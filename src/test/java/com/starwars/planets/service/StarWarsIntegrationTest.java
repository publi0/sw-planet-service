package com.starwars.planets.service;

import com.starwars.planets.dto.FoundPlanetIntegration;
import com.starwars.planets.exception.IntegrationException;
import com.starwars.planets.service.client.StarWarsApiClient;
import com.starwars.planets.service.client.response.SwClientFindPlanet;
import com.starwars.planets.service.client.response.SwClientFindPlanetResult;
import feign.FeignException;
import feign.Request;
import feign.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.starwars.planets.utils.TestUtils.getRandomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
public class StarWarsIntegrationTest {

	@Mock
	private StarWarsApiClient starWarsApiClientMock;

	@InjectMocks
	private StarWarsIntegration starWarsIntegration;

	@Test
	public void findOnePlanetByName_shouldFoundPlanetInformation() {
		final var planetName = getRandomString();
		final var foundFilms = Set.of(getRandomString(), getRandomString(), getRandomString());
		final var swClientFindPlanetResult = new SwClientFindPlanetResult(planetName, foundFilms);
		final var resultsFound = 1;
		final var swClientFindPlanet = new SwClientFindPlanet(resultsFound, Set.of(swClientFindPlanetResult));

		when(starWarsApiClientMock.findPlanetByName(planetName)).thenReturn(ResponseEntity.ok()
				.body(swClientFindPlanet));

		final var result = starWarsIntegration.findOnePlanetByName(planetName);

		assertEquals(Optional.of(new FoundPlanetIntegration(planetName, foundFilms.size())), result);

		verify(starWarsApiClientMock, times(1)).findPlanetByName(planetName);
	}

	@Test
	public void findOnePlanetByName_shouldFoundPlanetWithZeroAppearances() {
		final var planetName = getRandomString();
		final var foundFilms = Collections.EMPTY_SET;
		final var swClientFindPlanetResult = new SwClientFindPlanetResult(planetName, foundFilms);
		final var resultsFound = 1;
		final var swClientFindPlanet = new SwClientFindPlanet(resultsFound, Set.of(swClientFindPlanetResult));

		when(starWarsApiClientMock.findPlanetByName(planetName)).thenReturn(ResponseEntity.ok()
				.body(swClientFindPlanet));

		final var result = starWarsIntegration.findOnePlanetByName(planetName);

		assertEquals(Optional.of(new FoundPlanetIntegration(planetName, foundFilms.size())), result);

		verify(starWarsApiClientMock, times(1)).findPlanetByName(planetName);
	}

	@Test
	public void findOnePlanetByName_shouldFoundPlanetWithIncorrectName() {
		final var planetName = getRandomString();
		final var foundFilms = Set.of(getRandomString(), getRandomString(), getRandomString());
		final var swClientFindPlanetResult = new SwClientFindPlanetResult("wrongName", foundFilms);
		final var resultsFound = 1;
		final var swClientFindPlanet = new SwClientFindPlanet(resultsFound, Set.of(swClientFindPlanetResult));

		when(starWarsApiClientMock.findPlanetByName(planetName)).thenReturn(ResponseEntity.ok()
				.body(swClientFindPlanet));

		final var result = starWarsIntegration.findOnePlanetByName(planetName);

		assertEquals(Optional.empty(), result);

		verify(starWarsApiClientMock, times(1)).findPlanetByName(planetName);
	}

	@Test
	public void findOnePlanetByName_shouldReturnMoreThanOnePlanet() {
		final var planetName = getRandomString();
		final var secondPlanetName = getRandomString();
		final var foundFilms = Set.of(getRandomString(), getRandomString(), getRandomString());
		final var swClientFindPlanetResult1 = new SwClientFindPlanetResult(planetName, foundFilms);
		final var swClientFindPlanetResult2 = new SwClientFindPlanetResult(secondPlanetName, Collections.emptySet());
		final var resultsFound = 1;
		final var swClientFindPlanet = new SwClientFindPlanet(resultsFound,
				Set.of(swClientFindPlanetResult1, swClientFindPlanetResult2));

		when(starWarsApiClientMock.findPlanetByName(planetName)).thenReturn(ResponseEntity.ok()
				.body(swClientFindPlanet));

		final var result = starWarsIntegration.findOnePlanetByName(planetName);

		assertEquals(Optional.of(new FoundPlanetIntegration(planetName, foundFilms.size())), result);

		verify(starWarsApiClientMock, times(1)).findPlanetByName(planetName);
	}

	@Test
	public void findOnePlanetByName_shouldReturnNullValue() {
		final var planetName = getRandomString();
		final var foundFilms = Set.of(getRandomString(), getRandomString(), getRandomString());
		final var swClientFindPlanetResult = new SwClientFindPlanetResult(planetName, foundFilms);
		final var swClientFindPlanet = new SwClientFindPlanet(null, Set.of(swClientFindPlanetResult));

		when(starWarsApiClientMock.findPlanetByName(planetName)).thenReturn(ResponseEntity.ok()
				.body(swClientFindPlanet));

		final var assertThrows = assertThrows(IntegrationException.class,
				() -> starWarsIntegration.findOnePlanetByName(planetName));

		assertEquals("Error parsing content from Star Wars API", assertThrows.getMessage());

		verify(starWarsApiClientMock, times(1)).findPlanetByName(planetName);
	}

	@Test
	public void findOnePlanetByName_shouldReturnFeignExceptionOnCall() {
		final var planetName = getRandomString();
		final var request = Request.create(Request.HttpMethod.GET, "/api", Collections.emptyMap(), null, Util.UTF_8, null);
		when(starWarsApiClientMock.findPlanetByName(planetName)).thenThrow(
				new FeignException.BadRequest(BAD_REQUEST.toString(), request, BAD_REQUEST.toString()
						.getBytes()));

		final var assertThrows = assertThrows(IntegrationException.class,
				() -> starWarsIntegration.findOnePlanetByName(planetName));

		assertEquals("Error contacting Star Wars API", assertThrows.getMessage());

		verify(starWarsApiClientMock, times(1)).findPlanetByName(planetName);
	}
}