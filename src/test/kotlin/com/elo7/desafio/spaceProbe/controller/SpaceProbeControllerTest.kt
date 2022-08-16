package com.elo7.desafio.spaceProbe.controller

import com.elo7.desafio.*
import com.elo7.desafio.util.TestUtil
import org.hamcrest.Matchers
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

    @Test
    @Sql("/sql/insert-planet.sql", "/sql/insert-space-probe.sql")
    fun get() {
        mvc.perform(
            MockMvcRequestBuilders
                .get("$URL_BASE/$SPACE_PROBE_ID_1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.readJsonFile("space-probe-request.json"))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.id").isNotEmpty
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.position.x").value(SPACE_PROBE_POSITION_2)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.position.y").value(SPACE_PROBE_POSITION_3)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.position.direction").value(NORTH)
            )
    }

    @Test
    fun `Erro ao pegar sonda - sonda nao encontrada`() {
        mvc.perform(
            MockMvcRequestBuilders
                .get("$URL_BASE/$SPACE_PROBE_ID_1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)

    }

    @Test
    @Sql("/sql/insert-multi-planet-and-space-probe.sql")
    fun list() {
        mvc.perform(
            MockMvcRequestBuilders
                .get(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(10))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
    }

    @Test
    @Sql("/sql/insert-multi-planet-and-space-probe.sql")
    fun listByPlanet() {
        mvc.perform(
            MockMvcRequestBuilders
                .get("$URL_BASE?idPlanet=$PLANET_ID_2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(10))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1))
    }

    @Test
    @Sql("/sql/insert-planet.sql", "/sql/insert-space-probe.sql")
    fun delete() {
        mvc.perform(
            MockMvcRequestBuilders
                .delete("$URL_BASE/$SPACE_PROBE_ID_1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isAccepted)
    }

    @Test
    fun `Erro ao deletar - sonda nao encontrada`() {
        mvc.perform(
            MockMvcRequestBuilders
                .delete("$URL_BASE/$SPACE_PROBE_ID_1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    @Sql("/sql/insert-multi-planet-and-space-probe.sql")
    fun commandExecutionCase1() {
        mvc.perform(
            MockMvcRequestBuilders
                .patch("$URL_BASE/$SPACE_PROBE_ID_1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.readJsonFile("command1.json"))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message")
                    .value("Posição final da sonda: x=1 y=3 apontando para norte")
            )
    }

    @Test
    @Sql("/sql/insert-multi-planet-and-space-probe.sql")
    fun commandExecutionCase2() {
        mvc.perform(
            MockMvcRequestBuilders
                .patch("$URL_BASE/$SPACE_PROBE_ID_2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.readJsonFile("command2.json"))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message")
                    .value("Posição final da sonda: x=5 y=1 apontando para norte")
            )
    }

    @Test
    @Sql("/sql/insert-multi-planet-and-space-probe.sql")
    fun `Teste com erro - posicao invalida`() {
        mvc.perform(
            MockMvcRequestBuilders
                .patch("$URL_BASE/$SPACE_PROBE_ID_1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.readJsonFile("command3.json"))
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
    @Sql("/sql/insert-multi-planet-and-space-probe.sql")
    fun `Teste com erro - comando invalido`() {
        mvc.perform(
            MockMvcRequestBuilders
                .patch("$URL_BASE/$SPACE_PROBE_ID_1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.readJsonFile("invalid-command.json"))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(
                MockMvcResultMatchers.jsonPath(
                    "\$.message",
                    Matchers.equalTo("Cannot deserialize value of type `com.elo7.desafio.spaceProbe.request.Command` from String \"Z\": not one of the values accepted for Enum class: [R, L, M]")
                )
            )
    }

    @Test
    fun `Teste com erro - sonda nao encontrada`() {
        mvc.perform(
            MockMvcRequestBuilders
                .patch("$URL_BASE/$SPACE_PROBE_ID_1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.readJsonFile("command3.json"))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message")
                    .value("Sonda não encontrada")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.timestamp").isNotEmpty
            )
    }

}