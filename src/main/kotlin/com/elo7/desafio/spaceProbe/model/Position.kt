package com.elo7.desafio.spaceProbe.model

import org.springframework.validation.annotation.Validated
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.Positive

@Validated
@Embeddable
data class Position(
    var x: Int,
    var y: Int,

    @field:Column(nullable = false, length = 5)
    @field:Enumerated(EnumType.STRING)
    var direction: DirectionEnum,
) {
    fun move(action: Char) {
        if (action == 'M') {
            toWalk()
        } else {
            turn(action)
        }
    }

    private fun turn(to: Char) {
        direction = direction.turn(to)
    }

    private fun toWalk() {
        if (direction === DirectionEnum.NORTH || direction === DirectionEnum.SOUTH) y += direction.operation
        if (direction === DirectionEnum.EAST || direction === DirectionEnum.WEST) x += direction.operation
    }
}