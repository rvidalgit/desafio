package com.elo7.desafio.config.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest(classes = [JacksonConfig::class])
class JacksonConfigTest {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `Teste conversao de data para json`() {
        val mockDate = MockType(
            LocalDateTime.now().withYear(2021).withMonth(9).withDayOfMonth(27)
                .withHour(0).withMinute(0).withSecond(0).withNano(0), null
        )
        val expected = """{"date":"2021-09-27T00:00:00.000Z"}"""
        val jsonResult = objectMapper.writeValueAsString(mockDate)
        Assertions.assertNotNull(jsonResult)
        Assertions.assertEquals(jsonResult, expected)
    }

    @Test
    fun `Teste conversao datas com json para object`() {
        val mockJson = """{"date":"2021-09-27T00:00:00.000Z"}"""

        val dateExpected = LocalDateTime.now().withYear(2021).withMonth(9).withDayOfMonth(27)
            .withHour(0).withMinute(0).withSecond(0).withNano(0)
        val mockDate = objectMapper.readValue(mockJson, MockType::class.java)
        Assertions.assertNotNull(mockDate)
        Assertions.assertEquals(mockDate.date, dateExpected)
    }

}

data class MockType(
    val date: LocalDateTime? = null,
    val value: BigDecimal? = null
)