package com.starwars.planets.controller;

import com.starwars.planets.Application;
import com.starwars.planets.converter.PlanetConverter;
import com.starwars.planets.exception.ConflictException;
import com.starwars.planets.exception.DataNotFoundException;
import com.starwars.planets.model.Planet;
import com.starwars.planets.service.PlanetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static com.starwars.planets.utils.PlanetControllerUtils.getCreatePlanetRequestJson;
import static com.starwars.planets.utils.PlanetControllerUtils.getCreatePlanetRequestWithoutClimateJson;
import static com.starwars.planets.utils.PlanetControllerUtils.getCreatePlanetRequestWithoutNameJson;
import static com.starwars.planets.utils.PlanetControllerUtils.getCreatePlanetRequestWithoutTerrainJson;
import static com.starwars.planets.utils.TestUtils.getRandomInteger;
import static com.starwars.planets.utils.TestUtils.getRandomString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest({ PlanetController.class })
@ContextConfiguration(classes = { Application.class, PlanetConverter.class })
public class PlanetControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PlanetService planetServiceMock;

	@Test
	public void createPlanet_shouldCreate() throws Exception {
		final var requestJson = getCreatePlanetRequestJson();

		final var planetName = getRandomString();
		final var planetTerrain = getRandomString();
		final var planetClimate = getRandomString();
		final var planetId = getRandomString();
		final var planetUser = getRandomString();

		final var uuid = UUID.randomUUID();
		final var planet = new Planet(planetId, uuid.toString(), planetName, planetClimate, planetTerrain,
				LocalDateTime.now(), planetUser);

		final var incomePlanet = new Planet("Tatooine", "arid", "desert");
		when(planetServiceMock.createPlanet(incomePlanet)).thenReturn(planet);

		mockMvc.perform(post("/api/v1/star-wars/planets").contentType(APPLICATION_JSON)
				.content(requestJson))
				.andExpect(status().is(201))
				.andExpect(jsonPath("id").value(uuid.toString()));

		verify(planetServiceMock, times(1)).createPlanet(incomePlanet);
	}

	@Test
	public void createPlanet_shouldConflictWhenNameAlreadyExist() throws Exception {
		final var requestJson = getCreatePlanetRequestJson();
		final var incomePlanet = new Planet("Tatooine", "arid", "desert");
		when(planetServiceMock.createPlanet(incomePlanet)).thenThrow(new ConflictException("Planet already exist"));

		mockMvc.perform(post("/api/v1/star-wars/planets").contentType(APPLICATION_JSON)
				.content(requestJson))
				.andExpect(status().is(409))
				.andExpect(jsonPath("description").value("Planet already exist"));

		verify(planetServiceMock, times(1)).createPlanet(incomePlanet);
	}

	@Test
	public void createPlanet_shouldBadRequestWhenMissingName() throws Exception {
		final var requestJson = getCreatePlanetRequestWithoutNameJson();

		mockMvc.perform(post("/api/v1/star-wars/planets").contentType(APPLICATION_JSON)
				.content(requestJson))
				.andExpect(status().is(400))
				.andExpect(jsonPath("description").value("Validation Exception"))
				.andExpect(jsonPath("attributes[0].attribute").value("name"))
				.andExpect(jsonPath("attributes[0].message").value("Planet name cannot be null"));

		verify(planetServiceMock, times(0)).createPlanet(any());
	}

	@Test
	public void createPlanet_shouldBadRequestWhenMissingClimate() throws Exception {
		final var requestJson = getCreatePlanetRequestWithoutClimateJson();

		mockMvc.perform(post("/api/v1/star-wars/planets").contentType(APPLICATION_JSON)
				.content(requestJson))
				.andExpect(status().is(400))
				.andExpect(jsonPath("description").value("Validation Exception"))
				.andExpect(jsonPath("attributes[0].attribute").value("climate"))
				.andExpect(jsonPath("attributes[0].message").value("Planet climate cannot be null"));

		verify(planetServiceMock, times(0)).createPlanet(any());
	}

	@Test
	public void createPlanet_shouldBadRequestWhenMissingTerrain() throws Exception {
		final var requestJson = getCreatePlanetRequestWithoutTerrainJson();

		mockMvc.perform(post("/api/v1/star-wars/planets").contentType(APPLICATION_JSON)
				.content(requestJson))
				.andExpect(status().is(400))
				.andExpect(jsonPath("description").value("Validation Exception"))
				.andExpect(jsonPath("attributes[0].attribute").value("terrain"))
				.andExpect(jsonPath("attributes[0].message").value("Planet terrain cannot be null"));

		verify(planetServiceMock, times(0)).createPlanet(any());
	}

	@Test
	public void findPlanets_shouldFindWithoutName() throws Exception {
		final var planetName = getRandomString();
		final var planetTerrain = getRandomString();
		final var planetClimate = getRandomString();
		final var planetId = getRandomString();
		final var planetUser = getRandomString();

		final var uuid = UUID.randomUUID();
		final var planet = new Planet(planetId, uuid.toString(), planetName, planetClimate, planetTerrain,
				LocalDateTime.now(), planetUser);

		final var appearances = getRandomInteger();
		final var serviceResponse = Map.of(planet, appearances);

		when(planetServiceMock.findPlanetsWithFilmAppearances(null)).thenReturn(serviceResponse);

		mockMvc.perform(get("/api/v1/star-wars/planets").contentType(APPLICATION_JSON))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$[0].name").value(planetName))
				.andExpect(jsonPath("$[0].terrain").value(planetTerrain))
				.andExpect(jsonPath("$[0].climate").value(planetClimate))
				.andExpect(jsonPath("$[0].id").value(uuid.toString()))
				.andExpect(jsonPath("$[0].filmAppearances").value(appearances));

		verify(planetServiceMock, times(1)).findPlanetsWithFilmAppearances(null);
	}

	@Test
	public void findPlanets_shouldFindWithName() throws Exception {
		final var planetName = getRandomString();
		final var planetTerrain = getRandomString();
		final var planetClimate = getRandomString();
		final var planetId = getRandomString();
		final var planetUser = getRandomString();

		final var uuid = UUID.randomUUID();
		final var planet = new Planet(planetId, uuid.toString(), planetName, planetClimate, planetTerrain,
				LocalDateTime.now(), planetUser);

		final var appearances = getRandomInteger();
		final var serviceResponse = Map.of(planet, appearances);

		when(planetServiceMock.findPlanetsWithFilmAppearances(planetName)).thenReturn(serviceResponse);

		mockMvc.perform(get("/api/v1/star-wars/planets").contentType(APPLICATION_JSON)
				.param("name", planetName))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$[0].name").value(planetName))
				.andExpect(jsonPath("$[0].terrain").value(planetTerrain))
				.andExpect(jsonPath("$[0].climate").value(planetClimate))
				.andExpect(jsonPath("$[0].id").value(uuid.toString()))
				.andExpect(jsonPath("$[0].filmAppearances").value(appearances));

		verify(planetServiceMock, times(1)).findPlanetsWithFilmAppearances(planetName);
	}

	@Test
	public void findPlanets_shouldReturnEmpty() throws Exception {
		final var planetName = getRandomString();

		when(planetServiceMock.findPlanetsWithFilmAppearances(planetName)).thenReturn(Collections.emptyMap());

		mockMvc.perform(get("/api/v1/star-wars/planets").contentType(APPLICATION_JSON)
				.param("name", planetName))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$").isEmpty());

		verify(planetServiceMock, times(1)).findPlanetsWithFilmAppearances(planetName);
	}

	@Test
	public void findPlanetById_shouldFind() throws Exception {
		final var planetName = getRandomString();
		final var planetTerrain = getRandomString();
		final var planetClimate = getRandomString();
		final var planetId = getRandomString();
		final var planetUser = getRandomString();

		final var uuid = UUID.randomUUID();
		final var planet = new Planet(planetId, uuid.toString(), planetName, planetClimate, planetTerrain,
				LocalDateTime.now(), planetUser);

		final var appearances = getRandomInteger();
		final var serviceResponse = Map.entry(planet, appearances);

		when(planetServiceMock.findPlanetWithFilmAppearancesById(uuid.toString())).thenReturn(serviceResponse);

		mockMvc.perform(get("/api/v1/star-wars/planets/{id}", uuid.toString()).contentType(APPLICATION_JSON))
				.andExpect(status().is(200))
				.andExpect(jsonPath("name").value(planetName))
				.andExpect(jsonPath("terrain").value(planetTerrain))
				.andExpect(jsonPath("climate").value(planetClimate))
				.andExpect(jsonPath("id").value(uuid.toString()))
				.andExpect(jsonPath("filmAppearances").value(appearances));

		verify(planetServiceMock, times(1)).findPlanetWithFilmAppearancesById(uuid.toString());
	}

	@Test
	public void findPlanetById_shouldThrowNotFoundWhenNotFound() throws Exception {
		final var uuid = UUID.randomUUID();
		when(planetServiceMock.findPlanetWithFilmAppearancesById(uuid.toString())).thenThrow(
				new DataNotFoundException("Planet not found"));

		mockMvc.perform(get("/api/v1/star-wars/planets/{id}", uuid.toString()).contentType(APPLICATION_JSON))
				.andExpect(status().is(404))
				.andExpect(jsonPath("description").value("Planet not found"));

		verify(planetServiceMock, times(1)).findPlanetWithFilmAppearancesById(uuid.toString());
	}

	@Test
	public void deletePlanet_shouldDeletePlanet() throws Exception {
		final var uuid = UUID.randomUUID();
		doNothing().when(planetServiceMock)
				.deletePlanetById(uuid.toString());

		mockMvc.perform(delete("/api/v1/star-wars/planets/{id}", uuid.toString()).contentType(APPLICATION_JSON))
				.andExpect(status().is(204));

		verify(planetServiceMock, times(1)).deletePlanetById(uuid.toString());
	}

	@Test
	public void deletePlanet_shouldThrowNotFoundWhenNotFound() throws Exception {
		final var uuid = UUID.randomUUID();
		doThrow(new DataNotFoundException("Planet not found")).when(planetServiceMock)
				.deletePlanetById(uuid.toString());

		mockMvc.perform(delete("/api/v1/star-wars/planets/{id}", uuid.toString()).contentType(APPLICATION_JSON))
				.andExpect(status().is(404))
				.andExpect(jsonPath("description").value("Planet not found"));

		verify(planetServiceMock, times(1)).deletePlanetById(uuid.toString());
	}
}