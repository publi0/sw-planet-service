package com.starwars.planets.service.client.response;

import java.util.Set;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
public record SwClientFindPlanetResult(String name, Set<String> films) {
//
//	private String name;
//
//	private Set<String> films;
}