package com.elo7.desafio.spaceProbe.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DirectionTest {

    @Test
    fun testTurnLeft() {
        val n = Direction.NORTH.turn("L")
        val s = Direction.SOUTH.turn("L")
        val w = Direction.WEST.turn("L")
        val e = Direction.EAST.turn("L")
        assertEquals(Direction.WEST, n)
        assertEquals(Direction.EAST, s)
        assertEquals(Direction.SOUTH, w)
        assertEquals(Direction.NORTH, e)
    }

    @Test
    fun testTurnRight() {
        val n = Direction.NORTH.turn("R")
        val s = Direction.SOUTH.turn("R")
        val w = Direction.WEST.turn("R")
        val e = Direction.EAST.turn("R")
        assertEquals(Direction.EAST, n)
        assertEquals(Direction.WEST, s)
        assertEquals(Direction.NORTH, w)
        assertEquals(Direction.SOUTH, e)
    }

    @Test
    fun testGetOperation() {
        assertEquals(Direction.EAST.operation, 1)
        assertEquals(Direction.WEST.operation, -1)
        assertEquals(Direction.NORTH.operation, 1)
        assertEquals(Direction.SOUTH.operation, -1)

    }

    @Test
    fun testGetLabel() {
        assertEquals(Direction.EAST.label, "leste")
        assertEquals(Direction.WEST.label, "oeste")
        assertEquals(Direction.NORTH.label, "norte")
        assertEquals(Direction.SOUTH.label, "sul")
    }

    @Test
    fun findByLabel() {
        assertEquals(Direction.findByLabel("leste"), Direction.EAST)
        assertEquals(Direction.findByLabel("oeste"), Direction.WEST)
        assertEquals(Direction.findByLabel("Norte"), Direction.NORTH)
        assertEquals(Direction.findByLabel("Sul"), Direction.SOUTH)
    }
}