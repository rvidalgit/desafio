package com.elo7.desafio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class DesafioApplication

fun main(args: Array<String>) {
	runApplication<DesafioApplication>(*args)
}
