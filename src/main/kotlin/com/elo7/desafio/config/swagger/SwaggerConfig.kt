package com.elo7.desafio.config.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openApi(): OpenAPI =
        OpenAPI()
            .info(
                Info().title("Desafio Elo7")
                    .description("API desenvolvida para o desafio")
                    .version("v1")
                    .contact(
                        Contact()
                            .email("rodrigovidal2006@gmail.com")
                            .name("Rodrigo Vidal")
                            .url("https://github.com/rvidalgit/desafio")
                    )
            )

}