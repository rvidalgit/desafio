package com.elo7.desafio.spaceProbe.service

import com.elo7.desafio.*
import com.elo7.desafio.exception.NotFoundException
import com.elo7.desafio.exception.SpaceProbeCollidedException
import com.elo7.desafio.planet.model.Planet
import com.elo7.desafio.planet.repository.PlanetRepository
import com.elo7.desafio.spaceProbe.component.CommandInterpreterComponent
import com.elo7.desafio.spaceProbe.model.Direction
import com.elo7.desafio.spaceProbe.model.Position
import com.elo7.desafio.spaceProbe.model.SpaceProbe
import com.elo7.desafio.spaceProbe.repository.SpaceProbeRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.sql.SQLIntegrityConstraintViolationException
import java.util.*

@ExtendWith(MockitoExtension::class)
class SpaceProbeServiceTest {

    @Mock
    private lateinit var commandInterpreterComponent: CommandInterpreterComponent

    @Mock
    private lateinit var planetRepository: PlanetRepository

    @Mock
    private lateinit var spaceProbeRepository: SpaceProbeRepository

    @Spy
    private lateinit var spaceProbe: SpaceProbe

    @Mock
    private lateinit var spaceProbeSave: SpaceProbe

    @Mock
    private lateinit var planet: Planet

    @Mock
    private lateinit var dataIntegrityViolationException: DataIntegrityViolationException

    @Mock
    private lateinit var sqlIntegrityConstraintViolationException: SQLIntegrityConstraintViolationException

    @Mock
    private lateinit var position: Position

    @InjectMocks
    private lateinit var spaceProbeService: SpaceProbeService

    @Test
    fun `Testa a criacao de uma sonda`() {
        whenever(spaceProbe.planet).thenReturn(planet)
        whenever(planet.id).thenReturn(PLANET_ID_1)
        whenever(planetRepository.findById(PLANET_ID_1)).thenReturn(Optional.of(planet))
        whenever(spaceProbe.planet.height).thenReturn(PLANET_SIZE_Y)
        whenever(spaceProbe.planet.width).thenReturn(PLANET_SIZE_X)
        whenever(spaceProbe.position).thenReturn(position)
        whenever(spaceProbe.position.x).thenReturn(SPACE_PROBE_POSITION_2)
        whenever(spaceProbe.position.y).thenReturn(SPACE_PROBE_POSITION_3)
        whenever(spaceProbeRepository.save(spaceProbe)).thenReturn(spaceProbeSave)
        spaceProbeService.create(spaceProbe)
    }

    @Test
    fun `Erro ao criar uma sonda - 23505 - Colisao de sondas`() {
        whenever(spaceProbe.planet).thenReturn(planet)
        whenever(planet.id).thenReturn(PLANET_ID_1)
        whenever(planetRepository.findById(PLANET_ID_1)).thenReturn(Optional.of(planet))
        whenever(spaceProbe.planet.height).thenReturn(PLANET_SIZE_Y)
        whenever(spaceProbe.planet.width).thenReturn(PLANET_SIZE_X)
        whenever(spaceProbe.position).thenReturn(position)
        whenever(spaceProbe.position.x).thenReturn(SPACE_PROBE_POSITION_2)
        whenever(spaceProbe.position.y).thenReturn(SPACE_PROBE_POSITION_3)
        whenever(spaceProbeRepository.save(spaceProbe)).thenThrow(dataIntegrityViolationException)
        whenever(dataIntegrityViolationException.rootCause).thenReturn(sqlIntegrityConstraintViolationException)
        whenever(sqlIntegrityConstraintViolationException.sqlState).thenReturn("23505")
        assertThrows<SpaceProbeCollidedException> {
            spaceProbeService.create(spaceProbe)
        }
    }

    @Test
    fun `Erro ao criar uma sonda - 23506 - Planeta nao encontrado`() {
        whenever(spaceProbe.planet).thenReturn(planet)
        whenever(planet.id).thenReturn(PLANET_ID_1)
        whenever(planetRepository.findById(PLANET_ID_1)).thenReturn(Optional.of(planet))
        whenever(spaceProbe.planet.height).thenReturn(PLANET_SIZE_Y)
        whenever(spaceProbe.planet.width).thenReturn(PLANET_SIZE_X)
        whenever(spaceProbe.position).thenReturn(position)
        whenever(spaceProbe.position.x).thenReturn(SPACE_PROBE_POSITION_2)
        whenever(spaceProbe.position.y).thenReturn(SPACE_PROBE_POSITION_3)
        whenever(spaceProbeRepository.save(spaceProbe)).thenThrow(dataIntegrityViolationException)
        whenever(dataIntegrityViolationException.rootCause).thenReturn(sqlIntegrityConstraintViolationException)
        whenever(sqlIntegrityConstraintViolationException.sqlState).thenReturn("23506")
        assertThrows<NotFoundException> {
            spaceProbeService.create(spaceProbe)
        }
    }

    @Test
    fun `Erro desconhecido ao criar uma sonda`() {
        whenever(spaceProbe.planet).thenReturn(planet)
        whenever(planet.id).thenReturn(PLANET_ID_1)
        whenever(planetRepository.findById(any())).thenReturn(Optional.of(planet))
        whenever(spaceProbe.planet.height).thenReturn(PLANET_SIZE_Y)
        whenever(spaceProbe.planet.width).thenReturn(PLANET_SIZE_X)
        whenever(spaceProbe.position).thenReturn(position)
        whenever(spaceProbe.position.x).thenReturn(SPACE_PROBE_POSITION_2)
        whenever(spaceProbe.position.y).thenReturn(SPACE_PROBE_POSITION_3)
        whenever(spaceProbeRepository.save(spaceProbe)).thenThrow(dataIntegrityViolationException)
        whenever(dataIntegrityViolationException.rootCause).thenReturn(sqlIntegrityConstraintViolationException)
        whenever(sqlIntegrityConstraintViolationException.sqlState).thenReturn("000000")
        assertThrows<SQLIntegrityConstraintViolationException> {
            spaceProbeService.create(spaceProbe)
        }
    }

    @Test
    fun get() {
        whenever(spaceProbeRepository.findById(SPACE_PROBE_ID_1)).thenReturn(Optional.of(spaceProbe))
        assertTrue(spaceProbeService.get(SPACE_PROBE_ID_1).isPresent)
    }

    @Test
    fun `Lista todas as sondas de forma paginada`() {
        whenever(spaceProbeRepository.findAll(PageRequest.of(PAGE_0, SIZE_10))).thenReturn(PageImpl(listOf(spaceProbe)))
        val result = spaceProbeService.list(PAGE_0, SIZE_10)
        assertEquals(1, result.totalElements)
        assertTrue(result.isLast)
        assertTrue(result.isFirst)
    }

    @Test
    fun `Lista as sondas paginadas de um planeta`() {
        whenever(spaceProbeRepository.findAllByPlanet_Id(PLANET_ID_1, PageRequest.of(PAGE_0, SIZE_10))).thenReturn(
            PageImpl(
                listOf(spaceProbe)
            )
        )
        val result = spaceProbeService.list(PAGE_0, SIZE_10, PLANET_ID_1)
        assertEquals(1, result.totalElements)
        assertTrue(result.isLast)
        assertTrue(result.isFirst)
    }

    @Test
    fun executeCommand() {
        whenever(commandInterpreterComponent.splittedCommand(COMMAND)).thenReturn(charArrayOf('L', 'M'))
        whenever(spaceProbeRepository.findById(SPACE_PROBE_ID_1)).thenReturn(Optional.of(spaceProbe))
        whenever(spaceProbe.planet).thenReturn(planet)
        whenever(spaceProbe.planet.height).thenReturn(PLANET_SIZE_Y)
        whenever(spaceProbe.planet.width).thenReturn(PLANET_SIZE_X)
        whenever(spaceProbe.position).thenReturn(position)
        whenever(spaceProbe.position.x).thenReturn(SPACE_PROBE_POSITION_2)
        whenever(spaceProbe.position.y).thenReturn(SPACE_PROBE_POSITION_3)
        whenever(spaceProbe.position.direction).thenReturn(Direction.NORTH)
        val message = "Posição final da sonda: x=2 y=3 apontando para norte"
        val result = spaceProbeService.executeCommand(COMMAND, SPACE_PROBE_ID_1)
        assertEquals(message, result.message)
    }

    @Test
    fun `Erro ao executar comando - Sonda nao encontrada`() {
        whenever(commandInterpreterComponent.splittedCommand(COMMAND)).thenReturn(charArrayOf('L', 'M'))
        whenever(spaceProbeRepository.findById(SPACE_PROBE_ID_1)).thenReturn(Optional.empty())
        assertThrows<NotFoundException> {
            spaceProbeService.executeCommand(COMMAND, SPACE_PROBE_ID_1)
        }
    }

    @Test
    fun `Erro ao executar comando - Colisao de sondas`() {
        whenever(commandInterpreterComponent.splittedCommand(COMMAND)).thenReturn(charArrayOf('L', 'M'))
        whenever(spaceProbeRepository.findById(SPACE_PROBE_ID_1)).thenReturn(Optional.of(spaceProbe))
        whenever(spaceProbe.planet).thenReturn(planet)
        whenever(spaceProbe.planet.height).thenReturn(PLANET_SIZE_Y)
        whenever(spaceProbe.planet.width).thenReturn(PLANET_SIZE_X)
        whenever(spaceProbe.position).thenReturn(position)
        whenever(spaceProbe.position.x).thenReturn(SPACE_PROBE_POSITION_2)
        whenever(spaceProbe.position.y).thenReturn(SPACE_PROBE_POSITION_3)
        whenever(spaceProbe.position.direction).thenReturn(Direction.NORTH)
        whenever(spaceProbeRepository.save(spaceProbe)).thenThrow(dataIntegrityViolationException)
        whenever(dataIntegrityViolationException.rootCause).thenReturn(sqlIntegrityConstraintViolationException)
        whenever(sqlIntegrityConstraintViolationException.sqlState).thenReturn("23505")
        assertThrows<SpaceProbeCollidedException> {
            spaceProbeService.executeCommand(COMMAND, SPACE_PROBE_ID_1)
        }
    }

}