package com.starwars.planets.controller;

import com.starwars.planets.Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@WebMvcTest({ PlanetController.class })
@ContextConfiguration(classes = { Application.class })
public class PlanetControllerTest {

	@Test
	public void createPlanet_shouldCreate() {

	}

	@Test
	public void createPlanet_shouldBadRequestWhenMissingName() {

	}

	@Test
	public void createPlanet_shouldBadRequestWhenMissingClimate() {

	}

	@Test
	public void createPlanet_shouldBadRequestWhenMissingTerrain() {

	}

	@Test
	public void createPlanet_shouldConflictWhenNameAlreadyExist() {

	}

	@Test
	public void findPlanets_shouldFindWithOutName() {

	}

	@Test
	public void findPlanets_shouldFindWithName() {

	}

	@Test
	public void findPlanets_shouldReturnEmpty() {

	}

	@Test
	public void findPlanetById_shouldFind() {

	}

	@Test
	public void findPlanetById_shouldThrowNotFoundWhenNotFound() {

	}

	@Test
	public void deletePlanet_shouldDeletePlanet() {

	}

	@Test
	public void deletePlanet_shouldThrowNotFoundWhenNotFound() {

	}
}