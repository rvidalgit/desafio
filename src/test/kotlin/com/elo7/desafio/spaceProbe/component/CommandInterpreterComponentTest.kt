package com.elo7.desafio.spaceProbe.component

import com.elo7.desafio.exception.InvalidCommandException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

    @Test
    fun invalidCommand() {
        assertThrows<InvalidCommandException> {
            commandInterpreterComponent.splittedCommand("lMRZ")
        }
    }
}