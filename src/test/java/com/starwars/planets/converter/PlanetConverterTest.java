package com.starwars.planets.converter;

import com.starwars.planets.model.Planet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static com.starwars.planets.converter.PlanetConverter.convertToFindPlanetResponse;
import static com.starwars.planets.utils.TestUtils.getRandomInteger;
import static com.starwars.planets.utils.TestUtils.getRandomString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PlanetConverterTest {

	@Test
	public void convertToFindPlanetResponse_shouldConvertMapOfPlanets() {
		final var planetName = getRandomString();
		final var planetTerrain = getRandomString();
		final var planetClimate = getRandomString();
		final var planetId = getRandomString();
		final var planetUser = getRandomString();

		final var uuid = UUID.randomUUID();
		final var planet = new Planet(planetId, uuid.toString(), planetName, planetClimate, planetTerrain,
				LocalDateTime.now(), planetUser);

		final var appearances = getRandomInteger();
		final var planetsWithFilmAppearances = Map.of(planet, appearances);

		final var result = convertToFindPlanetResponse(planetsWithFilmAppearances);

		assertEquals(1, result.size());
		final var findPlanetResponse = result.stream()
				.findFirst()
				.get();
		assertEquals(planetName, findPlanetResponse.getName());
		assertEquals(planetClimate, findPlanetResponse.getClimate());
		assertEquals(planetTerrain, findPlanetResponse.getTerrain());
		assertEquals(uuid.toString(), findPlanetResponse.getId());
		assertEquals(appearances, findPlanetResponse.getFilmAppearances());
	}

	@Test
	public void convertToFindPlanetResponse_shouldConvertEntryOfPlanet() {
		final var planetName = getRandomString();
		final var planetTerrain = getRandomString();
		final var planetClimate = getRandomString();
		final var planetId = getRandomString();
		final var planetUser = getRandomString();

		final var uuid = UUID.randomUUID();
		final var planet = new Planet(planetId, uuid.toString(), planetName, planetClimate, planetTerrain,
				LocalDateTime.now(), planetUser);

		final var appearances = getRandomInteger();
		final var planetsWithFilmAppearances = Map.entry(planet, appearances);

		final var result = convertToFindPlanetResponse(planetsWithFilmAppearances);

		assertEquals(planetName, result.getName());
		assertEquals(planetClimate, result.getClimate());
		assertEquals(planetTerrain, result.getTerrain());
		assertEquals(uuid.toString(), result.getId());
		assertEquals(appearances, result.getFilmAppearances());
	}
}