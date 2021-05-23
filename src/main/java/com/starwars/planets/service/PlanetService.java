package com.starwars.planets.service;

import com.starwars.planets.Model.Planet;
import com.starwars.planets.PlanetProperties;
import com.starwars.planets.exception.ConflictException;
import com.starwars.planets.exception.DataNotFoundException;
import com.starwars.planets.repository.PlanetRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PlanetService {

	public static final int ZERO_APPEARANCES = 0;
	private final PlanetProperties properties;
	private final PlanetRepository planetRepository;
	private final StarWarsIntegration starWarsIntegration;

	public PlanetService(
			PlanetProperties properties, PlanetRepository planetRepository, StarWarsIntegration starWarsIntegration) {
		this.properties = properties;
		this.planetRepository = planetRepository;
		this.starWarsIntegration = starWarsIntegration;
	}

	public Planet createPlanet(Planet planet) {
		checkIfPlanetNameAlreadyExists(planet.getName());
		setPlanetBasicInformation(planet);

		log.info("Saving Planet...");
		return planetRepository.save(planet);
	}

	public Map<Planet, Integer> findPlanetsWithFilmAppearances(String name) {
		log.info("Find planets with film appearances");
		List<Planet> foundPlanets;

		if (name == null || name.isEmpty()) {
			log.info("Finding all planets");
			foundPlanets = planetRepository.findAll();
		} else {
			log.info("Find planets with param name [{}]", name);
			foundPlanets = planetRepository.findByNameIgnoreCase(name)
					.map(List::of)
					.orElseGet(Collections::emptyList);
		}

		return foundPlanets.stream()
				.map(this::findFilmAppearancesByPlanet)
				.collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));
	}

	public Entry<Planet, Integer> findPlanetWithFilmAppearancesById(String id) {
		log.info("Searching planet uuid");
		return planetRepository.findByUuid(id)
				.map(this::findFilmAppearancesByPlanet)
				.orElseThrow(() -> new DataNotFoundException("Planet not found"));
	}

	public void deletePlanetById(String planetId) {
		log.info("Searching planet uuid");
		final var foundPlanet = planetRepository.findByUuid(planetId)
				.orElseThrow(() -> new DataNotFoundException("Planet not found"));

		log.info("Planet found, deleting...");
		planetRepository.delete(foundPlanet);
	}

	private Entry<Planet, Integer> findFilmAppearancesByPlanet(Planet planet) {
		log.info("Searching planet in SW API with name [{}]", planet.getName());
		return starWarsIntegration.findOnePlanetByName(planet.getName())
				.map(y -> Map.entry(planet, y.filmAppearances()))
				.orElseGet(() -> Map.entry(planet, ZERO_APPEARANCES));
	}

	private void checkIfPlanetNameAlreadyExists(String name) {
		log.info("Checking if planet name [{}] already exists", name);
		planetRepository.findByNameIgnoreCase(name)
				.ifPresent(x -> {
					throw new ConflictException(String.format("Planet with name %s already exist", name));
				});
	}

	private void setPlanetBasicInformation(Planet planet) {
		log.info("Setting planet basic information...");
		planet.setCreateAt(LocalDateTime.now());
		planet.setCreatedBy(properties.getApplicationName());
		planet.setUuid(UUID.randomUUID()
				.toString());
	}
}
