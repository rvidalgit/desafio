package com.elo7.desafio.config.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openApi(): OpenAPI =
        OpenAPI()
            .servers(getServers())
            .info(getInfo())

    private fun getInfo(): Info {
        return Info().title("Desafio Elo7")
            .description("API desenvolvida para o desafio")
            .version("v1")
            .contact(
                Contact()
                    .email("rodrigovidal2006@gmail.com")
                    .name("Rodrigo Vidal")
                    .url("https://github.com/rvidalgit/desafio")
            )
    }

    private fun getServers(): MutableList<Server> {
        return mutableListOf(
            Server().url("http://localhost:8080").description("Development server"),
            Server().url("https://desafio-elo7.herokuapp.com").description("Production server")
        )
    }

}