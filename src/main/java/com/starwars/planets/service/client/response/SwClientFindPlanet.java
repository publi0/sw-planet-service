package com.starwars.planets.service.client.response;

import java.util.Set;

public record SwClientFindPlanet(Integer count, Set<SwClientFindPlanetResult> results) {
}
