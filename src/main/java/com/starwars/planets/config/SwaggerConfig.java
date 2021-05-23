package com.starwars.planets.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	public static final String DESCRIPTION = "Star Wars Planets Service";
	public static final String BASE_PACKAGE = "com.starwars.planets";
	public static final String TITLE = "Planets API";

	@Value("${info.app.version}")
	private String appVersion;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.useDefaultResponseMessages(false).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any())
				.build();
	}

	ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(TITLE)
				.description(DESCRIPTION)
				.version(appVersion)
				.build();
	}

}
