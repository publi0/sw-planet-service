package com.starwars.planets.service.client.response;

import java.util.Set;

public record SwClientFindPlanetResult(String name, Set<String> films) {
}