package com.elo7.desafio.spaceProbe.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DirectionEnumTest {

    @Test
    fun testTurnLeft() {
        val n = DirectionEnum.NORTH.turn('L')
        val s = DirectionEnum.SOUTH.turn('L')
        val w = DirectionEnum.WEST.turn('L')
        val e = DirectionEnum.EAST.turn('L')
        assertEquals(DirectionEnum.WEST, n)
        assertEquals(DirectionEnum.EAST, s)
        assertEquals(DirectionEnum.SOUTH, w)
        assertEquals(DirectionEnum.NORTH, e)
    }

    @Test
    fun testTurnRight() {
        val n = DirectionEnum.NORTH.turn('R')
        val s = DirectionEnum.SOUTH.turn('R')
        val w = DirectionEnum.WEST.turn('R')
        val e = DirectionEnum.EAST.turn('R')
        assertEquals(DirectionEnum.EAST, n)
        assertEquals(DirectionEnum.WEST, s)
        assertEquals(DirectionEnum.NORTH, w)
        assertEquals(DirectionEnum.SOUTH, e)
    }

    @Test
    fun testGetOperation(){
        assertEquals(DirectionEnum.EAST.operation, 1)
        assertEquals(DirectionEnum.WEST.operation, -1)
        assertEquals(DirectionEnum.NORTH.operation, 1)
        assertEquals(DirectionEnum.SOUTH.operation, -1)

    }

    @Test
    fun testGetLabel(){
        assertEquals(DirectionEnum.EAST.label, "leste")
        assertEquals(DirectionEnum.WEST.label, "oeste")
        assertEquals(DirectionEnum.NORTH.label, "norte")
        assertEquals(DirectionEnum.SOUTH.label, "sul")
    }

    @Test
    fun findByLabel() {
        assertEquals(DirectionEnum.findByLabel("leste"),DirectionEnum.EAST)
        assertEquals(DirectionEnum.findByLabel("oeste"),DirectionEnum.WEST)
        assertEquals(DirectionEnum.findByLabel("Norte"),DirectionEnum.NORTH)
        assertEquals(DirectionEnum.findByLabel("Sul"),DirectionEnum.SOUTH)
    }
}