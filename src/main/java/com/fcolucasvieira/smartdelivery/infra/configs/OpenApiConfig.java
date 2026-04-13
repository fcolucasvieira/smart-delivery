package com.fcolucasvieira.smartdelivery.infra.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("SmartDelivery API")
                        .version("1.0")
                        .description("""
                                SmartDelivery API provides endpoints for managing products, customers, and orders with JWT authentication.
                                
                                Main features:
                                - Product creation and listing
                                - Customer registration
                                - Order creation and update
                                - Asynchronous order processing with retry and DLQ
                                - Secure endpoints protected by JWT 
                                - External integration with ViaCEP API for address lookup
                                
                                Technologies:
                                Spring Boot • Spring Security • Spring Cloud • JWT • RabbitMQ • JPA/Hibernate • Flyway • Swagger • ViaCEP
                                """)
                                                    .contact(new Contact()
                                                            .name("Lucas Vieira")
                                                            .email("lucas.vieira@alu.ufc.br")
                                                            .url("https://github.com/fcolucasvieira/smartdelivery")
                                                    )
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                            .addSecuritySchemes(
                                    securitySchemeName,
                                    new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
