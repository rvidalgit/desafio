package com.elo7.desafio.spaceProbe.component

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [CommandInterpreterComponent::class])
class CommandInterpreterComponentTest {

    @Autowired
    private lateinit var commandInterpreterComponent: CommandInterpreterComponent

    @Test
    fun splittedCommand() {
        val result = commandInterpreterComponent.splittedCommand("lMLM")
        assertEquals(4, result.size)
        assertEquals('L', result[0])
    }
}