package com.starwars.planets.controller;

import com.starwars.planets.dto.request.CreatePlanetRequest;
import com.starwars.planets.dto.response.FindPlanetResponse;
import com.starwars.planets.model.Planet;
import com.starwars.planets.service.PlanetService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

import static com.starwars.planets.converter.PlanetConverter.convertToFindPlanetResponse;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@RestController
@Log4j2
@RequestMapping("/api/v1/star-wars/planets")
public class PlanetController extends AbstractRestController<String> {

	private final PlanetService planetService;

	public PlanetController(PlanetService planetService) {
		this.planetService = planetService;
	}

	@ApiOperation(value = "Create Planet")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_CREATED, message = "Planet created"),
			@ApiResponse(code = HttpServletResponse.SC_CONFLICT, message = "Conflict creating planet"),
			@ApiResponse(code = SC_BAD_REQUEST, message = "Invalid planet information"),
			@ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected error occurred!") })
	@PostMapping
	public ResponseEntity<String> createPlanet(
			@RequestBody
			@Valid @NotNull CreatePlanetRequest body) {
		log.info("Request for create planet: [{}]", body);

		final var createdPlanet = planetService.createPlanet(
				new Planet(body.getName(), body.getClimate(), body.getTerrain()));

		log.info("Planet create with success!");
		return newCreatedResponse(createdPlanet.getUuid());
	}

	@ApiOperation(value = "Find Planets")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "Return found planets"),
			@ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected error occurred!") })
	@GetMapping
	public ResponseEntity<Set<FindPlanetResponse>> findPlanets(
			@RequestParam(required = false)
					String name) {
		log.info("Request for find planets");

		final var planets = planetService.findPlanetsWithFilmAppearances(name);

		log.info("Returning result");
		return ResponseEntity.ok()
				.body(convertToFindPlanetResponse(planets));
	}

	@ApiOperation(value = "Find Planet By ID")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "Return found planet"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Planet not found"),
			@ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected error occurred!") })
	@GetMapping("/{id}")
	public ResponseEntity<FindPlanetResponse> findPlanetById(
			@PathVariable
					String id) {
		log.info("Request for find planet with id [{}]", id);
		final var foundPlanet = planetService.findPlanetWithFilmAppearancesById(id);

		log.info("Converting tuple to response");
		final var convertToFindPlanetResponse = convertToFindPlanetResponse(foundPlanet);

		log.info("Returning planet: [{}]", convertToFindPlanetResponse);
		return ResponseEntity.ok()
				.body(convertToFindPlanetResponse);
	}

	@ApiOperation(value = "Delete Planet By ID")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Planet deleted"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Planet not found"),
			@ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected error occurred!") })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePlanet(
			@PathVariable
					String id) {
		log.info("Request for delete planet with id [{}]", id);

		planetService.deletePlanetById(id);

		log.info("Planet deleted!");
		return ResponseEntity.noContent()
				.build();
	}
}
