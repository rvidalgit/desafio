package com.elo7.desafio.spaceProbe.controller

import com.elo7.desafio.config.exception.ValidationError
import com.elo7.desafio.spaceProbe.model.SpaceProbe
import com.elo7.desafio.spaceProbe.service.SpaceProbeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/spaceprobe")
class SpaceProbeController(
    val spaceProbeService: SpaceProbeService
) {

    @Operation(summary = "Responsável por criar uma sonda")
    @ApiResponses(
        ApiResponse(
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = SpaceProbe::class)
            )],
            responseCode = "201",
            description = "Sonda da cadastrada"
        ),
        ApiResponse(
            responseCode = "404", description = "Planeta não encontrado",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ValidationError::class)
            )]
        ),
        ApiResponse(
            responseCode = "422", description = "Colisão de sondas espaciais",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ValidationError::class)
            )]
        )
    )
    @PostMapping
    fun create(@Valid @RequestBody spaceProbe: SpaceProbe): ResponseEntity<SpaceProbe> {
        val spaceProbeSave = spaceProbeService.create(spaceProbe)
        return ResponseEntity
            .created(URI.create("/spaceprobe/${spaceProbeSave.id}"))
            .body(spaceProbeSave)
    }
}