package com.starwars.planets.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Planet {

	@Id
	private String id;
	private String uuid;
	private String name;
	private String climate;
	private String terrain;
	private LocalDateTime createAt;
	private String createdBy;

	public Planet(String name, String climate, String terrain) {
		this.name = name;
		this.climate = climate;
		this.terrain = terrain;
	}
}
