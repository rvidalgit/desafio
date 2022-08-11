package com.elo7.desafio.planet.controller

import com.elo7.desafio.config.exception.ValidationError
import com.elo7.desafio.planet.model.Planet
import com.elo7.desafio.planet.service.PlanetService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
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

    @Operation(summary = "Responsável por criar um planeta")
    @ApiResponse(
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = Planet::class)
        )],
        responseCode = "201"
    )
    @PostMapping
    fun createPlanet(@Valid @RequestBody planet: Planet): ResponseEntity<Planet> {
        val planetSave = planetService.create(planet)
        return ResponseEntity
            .created(URI.create("/planet/${planetSave.id}"))
            .body(planetSave)
    }

    @Operation(summary = "Responsável por retornar um planeta")
    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "Planeta encontrado", content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = Planet::class)
            )]
        ),
        ApiResponse(
            responseCode = "404", description = "Planeta não encontrado",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ValidationError::class)
            )]
        )
    )
    @GetMapping("/{id:\\d+}")
    fun get(@PathVariable id: Long): ResponseEntity<Planet> {
        return ResponseEntity.of(planetService.get(id))
    }

    @Operation(summary = "Responsável por retornar uma lista paginada de planetas")
    @ApiResponse(
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = Page::class)
        )],
        responseCode = "200"
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun list(
        @RequestParam("page", defaultValue = "0", required = false) page: Int,
        @RequestParam("size", defaultValue = "10", required = false) size: Int
    ): Iterable<Planet> {
        return planetService.list(page, size)
    }

    @Operation(summary = "Responsável por remover um planeta e suas sondas")
    @ApiResponses(
        ApiResponse(
            responseCode = "202", description = "Deleção aceita",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ValidationError::class)
            )]
        ),
        ApiResponse(
            responseCode = "404", description = "Planeta não encontrado",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ValidationError::class)
            )]
        )
    )
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