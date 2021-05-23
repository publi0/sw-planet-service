package com.starwars.planets.service;

import com.starwars.planets.dto.FoundPlanetIntegration;
import com.starwars.planets.exception.IntegrationException;
import com.starwars.planets.service.client.StarWarsApiClient;
import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Log4j2
public class StarWarsIntegration {

	private final StarWarsApiClient starWarsApiClient;

	public StarWarsIntegration(StarWarsApiClient starWarsApiClient) {
		this.starWarsApiClient = starWarsApiClient;
	}

	@Cacheable("sw-foundOnePlanet")
	public Optional<FoundPlanetIntegration> findOnePlanetByName(String name) {
		try {
			log.info("Calling SW API");
			final var responseEntity = starWarsApiClient.findPlanetByName(name);
			log.info("Retrieved code: {}", responseEntity.getStatusCode());
			final var body = responseEntity.getBody();

			if (body.count() > 1) {
				log.warn("Found more than one planet with name: [{}]", name);
			}

			return body.results()
					.stream()
					.findFirst()
					.filter(x -> x.name().equalsIgnoreCase(name))
					.map(x -> new FoundPlanetIntegration(x.name(), x.films()
							.size()));
		} catch (FeignException e) {
			throw new IntegrationException("Error contacting Star Wars API");
		} catch (NullPointerException e){
			throw new IntegrationException("Error parsing content from Star Wars API");
		}
	}
}
