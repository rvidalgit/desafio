package com.elo7.desafio.spaceProbe.component

import com.elo7.desafio.exception.InvalidCommandException
import org.springframework.stereotype.Component

@Component
class CommandInterpreterComponent {

    fun splittedCommand(actions: String): CharArray {
        return validated(actions.uppercase().toCharArray())
    }

    private fun validated(commands: CharArray): CharArray {
        commands.forEach { validCommands(it) }
        return commands
    }

    @Throws(InvalidCommandException::class)
    private fun validCommands(command: Char) {
        when (command) {
            'L', 'R', 'M' -> return
            else -> throw InvalidCommandException("O comando informado é inválido: '$command'")
        }

    }

}