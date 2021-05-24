package com.starwars.planets.converter;

import com.starwars.planets.dto.response.FindPlanetResponse;
import com.starwars.planets.model.Planet;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PlanetConverter {

	public static Set<FindPlanetResponse> convertToFindPlanetResponse(Map<Planet, Integer> planetsWithFilmAppearances) {
		return planetsWithFilmAppearances.entrySet()
				.stream()
				.map(x -> FindPlanetResponse.builder()
						.filmAppearances(x.getValue())
						.climate(x.getKey()
								.getClimate())
						.name(x.getKey()
								.getName())
						.terrain(x.getKey()
								.getTerrain())
						.id(x.getKey()
								.getUuid())
						.build())
				.collect(Collectors.toUnmodifiableSet());
	}

	public static FindPlanetResponse convertToFindPlanetResponse(Map.Entry<Planet, Integer> planetWithFilmAppearances) {
		return FindPlanetResponse.builder()
				.filmAppearances(planetWithFilmAppearances.getValue())
				.climate(planetWithFilmAppearances.getKey()
						.getClimate())
				.name(planetWithFilmAppearances.getKey()
						.getName())
				.terrain(planetWithFilmAppearances.getKey()
						.getTerrain())
				.id(planetWithFilmAppearances.getKey()
						.getUuid())
				.build();
	}
}
