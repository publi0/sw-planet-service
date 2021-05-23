package com.starwars.planets.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePlanetRequest {

	@ApiModelProperty(notes = "Planet name", example = "Tatooine", required = true)
	@NotNull(message = "Planet name cannot be null")
	private String name;

	@ApiModelProperty(notes = "Planet terrain", example = "desert", required = true)
	@NotNull(message = "Planet terrain cannot be null")
	private String terrain;

	@ApiModelProperty(notes = "Planet climate", example = "arid", required = true)
	@NotNull(message = "Planet climate cannot be null")
	private String climate;
}
