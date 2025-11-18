package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "User Registration & Authentication API",
                description = "Modern REST API for user registration, email verification, and JWT-based authentication",
                version = "1.0.0",
                contact = @Contact(
                        name = "API Support",
                        email = "support@example.com"
                )
        ),
        servers = {
                @Server(
                        description = "Local Development",
                        url = "http://localhost:8484/registration/v1"
                ),
                @Server(
                        description = "Production",
                        url = "https://api.example.com"
                )
        }
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
