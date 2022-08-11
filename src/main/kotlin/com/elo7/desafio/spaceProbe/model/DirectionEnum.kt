package com.elo7.desafio.spaceProbe.model

enum class DirectionEnum(val label: String, val operation: Int) {
    NORTH("norte", 1) {
        override fun turn(direction: Char): DirectionEnum =
            if (direction == 'L') WEST else EAST
    },
    SOUTH("sul", -1) {
        override fun turn(direction: Char): DirectionEnum =
            if (direction == 'L') EAST else WEST
    },
    WEST("oeste", -1) {
        override fun turn(direction: Char): DirectionEnum =
            if (direction == 'L') SOUTH else NORTH
    },
    EAST("leste", 1) {
        override fun turn(direction: Char): DirectionEnum =
            if (direction == 'L') NORTH else SOUTH
    };

    companion object {
        fun findByLabel(text: String): DirectionEnum? =
            values().firstOrNull { it.label.equals(text, true) }
    }

    abstract fun turn(direction: Char): DirectionEnum
}