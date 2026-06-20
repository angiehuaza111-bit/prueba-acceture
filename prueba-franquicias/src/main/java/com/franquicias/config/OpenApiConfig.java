package com.franquicias.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Franquicias")
                        .version("1.0.0")
                        .description("API reactiva para la gestión de franquicias, sucursales y productos. " +
                                "Permite crear franquicias, agregar sucursales y productos, " +
                                "actualizar nombres, modificar stock, eliminar productos, " +
                                "y consultar el producto con más stock por sucursal.")
                        .contact(new Contact()
                                .name("Accenture - Prueba Técnica")));
    }
}
