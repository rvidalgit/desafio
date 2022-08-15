package com.elo7.desafio.spaceProbe.controller

import com.elo7.desafio.NORTH
import com.elo7.desafio.SPACE_PROBE_ID_1
import com.elo7.desafio.SPACE_PROBE_POSITION_1
import com.elo7.desafio.SPACE_PROBE_POSITION_2
import com.elo7.desafio.util.TestUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SpaceProbeControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    private val URL_BASE = "/spaceprobe"

    @Test
    @Sql("/sql/insert-planet.sql")
    fun `Testa a criacao de uma sonda`() {
        mvc.perform(
            MockMvcRequestBuilders
                .post(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.readJsonFile("space-probe-request.json"))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.id").value(SPACE_PROBE_ID_1)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.position.x").value(SPACE_PROBE_POSITION_1)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.position.y").value(SPACE_PROBE_POSITION_2)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.position.direction").value(NORTH)
            )
    }

    @Test
    @Sql("/sql/insert-planet.sql")
    fun `Erro ao criar sonda - fora do limite do planeta`() {
        mvc.perform(
            MockMvcRequestBuilders
                .post(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.readJsonFile("space-probe-out-planet-request.json"))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message")
                    .value("Essas instruções iriam levar a sonda para uma posição inválida")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.timestamp").isNotEmpty
            )
    }

    @Test
    fun `Erro ao criar sonda - planeta nao encontrado`() {
        mvc.perform(
            MockMvcRequestBuilders
                .post(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.readJsonFile("space-probe-request.json"))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message")
                    .value("Planeta não encontrado")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.timestamp").isNotEmpty
            )
    }

    @Test
    @Sql("/sql/insert-planet.sql", "/sql/insert-space-probe.sql")
    fun `Erro ao criar sonda - ja existe uma sonda na posicao`() {
        mvc.perform(
            MockMvcRequestBuilders
                .post(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.readJsonFile("space-probe-request.json"))
        )
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message")
                    .value("Está operação causará uma colisão entre as sondas")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.timestamp").isNotEmpty
            )
    }


}