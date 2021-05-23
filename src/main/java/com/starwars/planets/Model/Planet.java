package com.starwars.planets.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
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
}
