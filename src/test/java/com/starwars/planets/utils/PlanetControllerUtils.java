package com.starwars.planets.utils;

public class PlanetControllerUtils {

	public static String getCreatePlanetRequestJson() {
		return """
				   {
				        "name": "Tatooine",
				        "terrain": "desert",
				        "climate": "arid"
				    }
				""";
	}

	public static String getCreatePlanetRequestWithoutNameJson() {
		return """
				   {
				        "terrain": "desert",
				        "climate": "arid"
				    }
				""";
	}

	public static String getCreatePlanetRequestWithoutTerrainJson() {
		return """
				   {
				        "name": "Tatooine",
				        "climate": "arid"
				    }
				""";
	}

	public static String getCreatePlanetRequestWithoutClimateJson() {
		return """
				   {
				        "name": "Tatooine",
				        "terrain": "desert"
				    }
				""";
	}
}
