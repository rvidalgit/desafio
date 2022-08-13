package com.elo7.desafio.spaceProbe.extension

import com.elo7.desafio.spaceProbe.model.DirectionEnum
import com.elo7.desafio.spaceProbe.model.Position

fun Position.move(action: Char) {
    if (action == 'M') {
        toWalk()
    } else {
        turn(action)
    }
}

private fun Position.turn(to: Char) {
    direction = direction.turn(to)
}

private fun Position.toWalk() {
    if (direction === DirectionEnum.NORTH || direction === DirectionEnum.SOUTH) y += direction.operation
    if (direction === DirectionEnum.EAST || direction === DirectionEnum.WEST) x += direction.operation
}