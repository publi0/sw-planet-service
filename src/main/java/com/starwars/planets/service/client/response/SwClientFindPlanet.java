package com.starwars.planets.service.client.response;

import java.util.Set;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
public record SwClientFindPlanet(Integer count, Set<SwClientFindPlanetResult> results) {

//	private Integer count;
//
//	private Set<SwClientFindPlanetResult> results;
}
