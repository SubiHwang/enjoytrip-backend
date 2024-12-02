package com.ssafy.enjoytrip.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

//Swagger-UI 확인
//http://localhost/swagger-ui.html

@Configuration
public class SwaggerConfiguration {

	// private static final Logger logger =
	// LoggerFactory.getLogger(SwaggerConfiguration.class);

	@Bean
	public OpenAPI openAPI() {
		final String securitySchemeName = "bearerAuth";

		Info info = new Info().title("Enjoy Trip API 명세서").description(
				"<h3>SSAFY API Reference for Developers</h3>Swagger를 이용한 Enjoy ")
				.version("v1");

		return new OpenAPI().components(new Components()).info(info)
				.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
				.components(new io.swagger.v3.oas.models.Components())
				.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
				.components(
						new Components()
								.addSecuritySchemes(securitySchemeName, new SecurityScheme()
										.name(securitySchemeName)
										.type(SecurityScheme.Type.HTTP)
										.scheme("bearer")
										.bearerFormat("JWT")));
	}
	
	@Bean
	public GroupedOpenApi allApis() {
		return GroupedOpenApi.builder().group("ssafy-all").pathsToMatch("/**").build();
	}

	@Bean
	public GroupedOpenApi memberApi() {
		return GroupedOpenApi.builder().group("ssafy-member").pathsToMatch("/member/**").build();
	}

	@Bean
	public GroupedOpenApi tripApi() {
		return GroupedOpenApi.builder().group("ssafy-trip").pathsToMatch("/trip/**").build();
	}
	
	@Bean
	public GroupedOpenApi boardApi() {
		return GroupedOpenApi.builder().group("ssafy-board").pathsToMatch("/board/**").build();
	}
	
	@Bean
	public GroupedOpenApi followApi() {
		return GroupedOpenApi.builder().group("ssafy-follow").pathsToMatch("/follows/**").build();
	}
	
	@Bean
	public GroupedOpenApi tripDirayApi() {
		return GroupedOpenApi.builder().group("ssafy-diary").pathsToMatch("/trip-diary/**").build();
	}

}