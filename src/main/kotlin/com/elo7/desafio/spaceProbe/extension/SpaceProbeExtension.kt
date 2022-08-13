package com.elo7.desafio.spaceProbe

import com.elo7.desafio.spaceProbe.model.SpaceProbe
import kotlin.math.abs

fun SpaceProbe.isInvalidPosition(): Boolean{
  return this.planet.height < abs(this.position.y) || this.planet.width < abs(this.position.x)
}