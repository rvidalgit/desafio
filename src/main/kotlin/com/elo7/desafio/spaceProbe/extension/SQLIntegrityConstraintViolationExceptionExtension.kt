package com.elo7.desafio.spaceProbe.extension

import com.elo7.desafio.exception.NotFoundException
import com.elo7.desafio.exception.SpaceProbeCollidedException
import java.sql.SQLIntegrityConstraintViolationException

@Throws(Exception::class)
fun SQLIntegrityConstraintViolationException.checkCause(): Exception {

    when (this.sqlState) {
        "23505" -> throw SpaceProbeCollidedException("Está operação causará uma colisão entre as sondas")
        "23506" -> throw NotFoundException("Planeta não encontrado")
        else -> throw this
    }

}