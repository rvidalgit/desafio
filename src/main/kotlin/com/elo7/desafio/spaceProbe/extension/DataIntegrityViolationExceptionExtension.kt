package com.elo7.desafio.spaceProbe.extension

import org.springframework.dao.DataIntegrityViolationException
import java.sql.SQLIntegrityConstraintViolationException

fun DataIntegrityViolationException.specificCause(): SQLIntegrityConstraintViolationException {
    val cause = this.rootCause
    return cause as SQLIntegrityConstraintViolationException
}