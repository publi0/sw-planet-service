package com.starwars.planets.repository;

import com.starwars.planets.Model.Planet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanetRepository extends MongoRepository<Planet, String> {

	Optional<Planet> findByNameIgnoreCase(String name);

	Optional<Planet> findByUuid(String uuid);
}
