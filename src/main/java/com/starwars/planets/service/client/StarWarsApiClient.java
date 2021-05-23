package com.starwars.planets.service.client;

import com.starwars.planets.service.client.response.SwClientFindPlanet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${sw.api.url}", name = "sw-client")
public interface StarWarsApiClient {

	@GetMapping(value = "/planets/", produces = "application/json")
	ResponseEntity<SwClientFindPlanet> findPlanetByName(
			@RequestParam(value = "search")
					String name);
}
