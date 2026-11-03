package br.com.biblioteca.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bibliotecaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Biblioteca API")
                .description("API REST para gerenciar o acervo de livros de uma biblioteca.")
                .version("0.0.1"));
    }
}
