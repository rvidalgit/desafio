package com.elo7.desafio.spaceProbe.model

enum class Direction(val label: String, val operation: Int) {
    NORTH("norte", 1) {
        override fun turn(direction: String): Direction =
            if (direction == "L") WEST else EAST
    },
    SOUTH("sul", -1) {
        override fun turn(direction: String): Direction =
            if (direction == "L") EAST else WEST
    },
    WEST("oeste", -1) {
        override fun turn(direction: String): Direction =
            if (direction == "L") SOUTH else NORTH
    },
    EAST("leste", 1) {
        override fun turn(direction: String): Direction =
            if (direction == "L") NORTH else SOUTH
    };

    companion object {
        fun findByLabel(text: String): Direction? =
            values().firstOrNull { it.label.equals(text, true) }
    }

    abstract fun turn(direction: String): Direction
}