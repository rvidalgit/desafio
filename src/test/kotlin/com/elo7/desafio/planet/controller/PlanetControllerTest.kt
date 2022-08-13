package com.elo7.desafio.planet.controller

import com.elo7.desafio.PLANET_ID_1
import com.elo7.desafio.PLANET_ID_2
import com.elo7.desafio.planet.model.Planet
import com.elo7.desafio.planet.repository.PlanetRepository
import com.elo7.desafio.spaceProbe.model.DirectionEnum
import com.elo7.desafio.spaceProbe.model.Position
import com.elo7.desafio.spaceProbe.model.SpaceProbe
import com.elo7.desafio.spaceProbe.repository.SpaceProbeRepository
import com.elo7.desafio.util.TestUtil.readJsonFile
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class PlanetControllerTest {

    @Autowired
    lateinit var planetRepository: PlanetRepository

    @Autowired
    lateinit var spaceProbeRepository: SpaceProbeRepository

    @Autowired
    lateinit var mvc: MockMvc

    private val URL_BASE = "/planet"

    @AfterEach
    fun tearDown() {
        planetRepository.deleteAll()
    }

    @Test
    fun `Teste de criacao do planeta`() {
        mvc.perform(
            MockMvcRequestBuilders
                .post(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(readJsonFile("planet-request.json"))
        )
            .andExpect(status().isCreated)
            .andExpect(
                jsonPath("\$.id").value(PLANET_ID_1)
            )
    }

    @Test
    fun `Teste para recuperar um pleneta`() {
        createPlanet()
        mvc.perform(
            MockMvcRequestBuilders
                .get("$URL_BASE/$PLANET_ID_1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(
                jsonPath("\$.id").value(PLANET_ID_1)
            )
    }

    @Test
    fun `Erro ao recuperar um planeta`() {
        createPlanet()
        mvc.perform(
            MockMvcRequestBuilders
                .get("$URL_BASE/$PLANET_ID_2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Lista os planetas de forma paginada`() {
        createPlanet()
        mvc.perform(
            MockMvcRequestBuilders
                .get(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.number").value(0))
            .andExpect(jsonPath("$.size").value(10))
            .andExpect(jsonPath("$.totalElements").value(1))
    }

    @Test
    fun `Testa a remocao de um planeta`() {
        createPlanet()
        mvc.perform(
            MockMvcRequestBuilders
                .delete("$URL_BASE/$PLANET_ID_1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isAccepted)
    }

    @Test
    fun `Testa a remocao de um planeta com uma sonda`() {
        createPlanetAndSpaceProbe()
        mvc.perform(
            MockMvcRequestBuilders
                .delete("$URL_BASE/$PLANET_ID_1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isAccepted)
    }

    @Test
    fun `Erro ao remover um planeta`() {
        mvc.perform(
            MockMvcRequestBuilders
                .delete("$URL_BASE/$PLANET_ID_1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    private fun createPlanet(): Planet {
        val planet = Planet(null, 5, 5, null, null, null)
        return planetRepository.save(planet)
    }

    private fun createPlanetAndSpaceProbe() {
        val planet = createPlanet()
        val spaceProbe = SpaceProbe(
            null,
            Position(1, 2, DirectionEnum.NORTH),
            null,
            null,
            planet
        )
        spaceProbeRepository.save(spaceProbe)
    }
}