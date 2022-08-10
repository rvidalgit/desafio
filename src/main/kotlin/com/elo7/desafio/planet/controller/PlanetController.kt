package com.elo7.desafio.planet.controller

import com.elo7.desafio.planet.model.Planet
import com.elo7.desafio.planet.service.PlanetService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/planet")
class PlanetController(
    val planetService: PlanetService
) {

    @PostMapping
    fun createPlanet(@Valid @RequestBody planet: Planet): ResponseEntity<Planet> {
        val planetSave = planetService.create(planet)
        return ResponseEntity
            .created(URI.create("/planet/${planetSave.id}"))
            .body(planetSave)
    }
}