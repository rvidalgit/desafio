package com.elo7.desafio.spaceProbe.component

import org.springframework.stereotype.Component

@Component
class CommandInterpreterComponent {

    fun splittedCommand(actions: String): CharArray {
        return actions.uppercase().toCharArray()
    }

}