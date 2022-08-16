package com.elo7.desafio.spaceProbe.extension

import com.elo7.desafio.spaceProbe.model.Direction
import com.elo7.desafio.spaceProbe.model.Position

fun Position.move(action: String) {
    if (action == "M") {
        toWalk()
    } else {
        turn(action)
    }
}

private fun Position.turn(to: String) {
    direction = direction.turn(to)
}

private fun Position.toWalk() {
    if (direction === Direction.NORTH || direction === Direction.SOUTH) y += direction.operation
    if (direction === Direction.EAST || direction === Direction.WEST) x += direction.operation
}