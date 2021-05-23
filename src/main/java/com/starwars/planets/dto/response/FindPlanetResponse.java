package com.starwars.planets.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindPlanetResponse {

	@ApiModelProperty(notes = "Planet id", example = "34")
	private String id;

	@ApiModelProperty(notes = "Planet name", example = "Tatooine")
	private String name;

	@ApiModelProperty(notes = "Planet terrain", example = "desert")
	private String terrain;

	@ApiModelProperty(notes = "Planet climate", example = "arid")
	private String climate;

	@ApiModelProperty(notes = "Quantity of film appearances", example = "3")
	private Integer filmAppearances;
}
