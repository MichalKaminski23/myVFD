package com.vfd.server.configs

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI = OpenAPI()
        .servers(
            listOf(
                Server().url("http://localhost:8080").description("Local")
            )
        )
        .components(
            Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Use 'Bearer &lt;JWT&gt;'")
                )
                .addResponses("BadRequest", ApiResponse().apply { description = "Validation error" })
                .addResponses("Unauthorized", ApiResponse().apply { description = "Unauthorized" })
                .addResponses("Forbidden", ApiResponse().apply { description = "Forbidden" })
                .addResponses("NotFound", ApiResponse().apply { description = "Resource not found" })
                .addResponses("Conflict", ApiResponse().apply { description = "Conflict" })
        )
        .addSecurityItem(
            SecurityRequirement().addList("bearerAuth")
        )
        .info(
            Info()
                .title("VFD Server API")
                .version("1.0")
                .description("API for Volunteer Fire Department management")
                .contact(Contact().name("VFD Team").email("support@vfd.example"))
                .license(License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
        )
        .externalDocs(
            ExternalDocumentation()
                .description("Project README")
                .url("https://github.com/MichalKaminski23/myVFD/blob/main/README.md")
        )

    @Bean
    fun devApi(): GroupedOpenApi =
        GroupedOpenApi.builder()
            .group("Dev")
            .packagesToScan("com.vfd.server.controllers.dev")
            .pathsToMatch("/api/dev/**")
            .build()

    @Bean
    fun restApi(): GroupedOpenApi =
        GroupedOpenApi.builder()
            .group("Rest")
            .packagesToScan("com.vfd.server.controllers")
            .pathsToExclude("/api/dev/**")
            .build()
}
