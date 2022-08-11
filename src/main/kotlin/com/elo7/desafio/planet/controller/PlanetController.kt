package com.elo7.desafio.planet.controller

import com.elo7.desafio.planet.model.Planet
import com.elo7.desafio.planet.service.PlanetService
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @GetMapping("/{id:\\d+}")
    fun get(@PathVariable id: Long): ResponseEntity<Planet> {
        return ResponseEntity.of(planetService.get(id))
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun list(
        @RequestParam("page", defaultValue = "0", required = false) page: Int,
        @RequestParam("size", defaultValue = "10", required = false) size: Int
    ): Iterable<Planet> {
        return planetService.list(page, size)
    }

    @DeleteMapping("/{id:\\d+}")
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            planetService.delete(id)
            ResponseEntity.status(HttpStatus.ACCEPTED).build()
        } catch (ex: EmptyResultDataAccessException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}