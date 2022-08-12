package com.elo7.desafio.planet.service

import com.elo7.desafio.PAGE_0
import com.elo7.desafio.PLANET_ID_1
import com.elo7.desafio.SIZE_10
import com.elo7.desafio.planet.model.Planet
import com.elo7.desafio.planet.repository.PlanetRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@ExtendWith(MockitoExtension::class)
class PlanetServiceTest {

    @Mock
    lateinit var planetRepository: PlanetRepository

    @Mock
    lateinit var planet: Planet

    @InjectMocks
    lateinit var planetService: PlanetService

    @Test
    fun create() {
        val planetSave = spy(Planet(1, 5, 5, null, null, null))
        whenever(planetRepository.save(planet)).thenReturn(planetSave)
        val result = planetService.create(planet)
        assertNotNull(result.id)
    }

    @Test
    fun get() {
        whenever(planetRepository.findById(PLANET_ID_1)).thenReturn(Optional.of(planet))
        val result = planetService.get(PLANET_ID_1)
        assertTrue(result.isPresent)
    }

    @Test
    fun list() {
        whenever(planetRepository.findAll(PageRequest.of(PAGE_0, SIZE_10))).thenReturn(PageImpl(listOf(planet)))
        val result = planetService.list(PAGE_0, SIZE_10)
        assertTrue(result.isFirst)
        assertTrue(result.isLast)
        assertFalse(result.isEmpty)
    }

}