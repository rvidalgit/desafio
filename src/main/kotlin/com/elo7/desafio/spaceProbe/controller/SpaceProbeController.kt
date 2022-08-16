package com.elo7.desafio.spaceProbe.controller

import com.elo7.desafio.config.exception.ValidationError
import com.elo7.desafio.spaceProbe.model.SpaceProbe
import com.elo7.desafio.spaceProbe.request.CommandRequest
import com.elo7.desafio.spaceProbe.response.Message
import com.elo7.desafio.spaceProbe.service.SpaceProbeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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
            responseCode = "400", description = "Posição inválida, fora das dimensões do planeta",
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

    @Operation(summary = "Responsável por retornar uma sonda")
    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "Sonda encontrada", content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = SpaceProbe::class)
            )]
        ),
        ApiResponse(
            responseCode = "404", description = "Sonda não encontrada",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ValidationError::class)
            )]
        )
    )
    @GetMapping("/{id:\\d+}")
    fun get(@PathVariable id: Long): ResponseEntity<SpaceProbe> {
        return ResponseEntity.of(spaceProbeService.get(id))
    }

    @Operation(
        summary = "Responsável por retornar uma lista paginada de sondas",
        parameters = [
            Parameter(
                name = "idPlanet",
                required = false,
                description = "Id do planeta a ser filtrado",
                schema = Schema(implementation = Long::class)
            ),
            Parameter(
                name = "size",
                description = "Tamanho da lista",
            ),
            Parameter(
                name = "page",
                description = "Número da página",
            )
        ]
    )
    @ApiResponse(
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = Page::class),
        )],
        responseCode = "200",
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun list(
        @RequestParam("page", defaultValue = "0", required = false) page: Int,
        @RequestParam("size", defaultValue = "10", required = false) size: Int,
        @RequestParam(required = false) idPlanet: Long?
    ): Page<SpaceProbe> {
        return if (idPlanet == null) {
            spaceProbeService.list(page, size)
        } else {
            spaceProbeService.list(page, size, idPlanet)
        }
    }

    @Operation(summary = "Responsável por remover uma sonda")
    @ApiResponses(
        ApiResponse(
            responseCode = "202", description = "Deleção aceita",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ValidationError::class)
            )]
        ),
        ApiResponse(
            responseCode = "404", description = "Sonda não encontrada",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ValidationError::class)
            )]
        )
    )
    @DeleteMapping("/{id:\\d+}")
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            spaceProbeService.delete(id)
            ResponseEntity.status(HttpStatus.ACCEPTED).build()
        } catch (ex: EmptyResultDataAccessException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @Operation(summary = "Responsável por receber os comandos de operação da sonda")
    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "Comando executado",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = Message::class)
            )]
        ),
        ApiResponse(
            responseCode = "404", description = "Sonda não encontrada",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ValidationError::class)
            )]
        ),
        ApiResponse(
            responseCode = "400", description = "Posição inválida, fora das dimensões do planeta",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ValidationError::class)
            )]
        ),
        ApiResponse(
            responseCode = "400", description = "Comando inválido",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ValidationError::class)
            )]
        )
    )
    @PatchMapping("/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    fun commandExecution(
        @Valid @RequestBody send: CommandRequest,
        @PathVariable id: Long
    ): Message {
        return spaceProbeService.executeCommand(send.command, id)
    }
}