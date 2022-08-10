package com.elo7.desafio.config.exception

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class ErrorResponse(
    status: HttpStatus,
    var message: String?,
    var errors: List<ValidationError>?
) {

    private val code: Int
    private val statusCode: String

    val timestamp = LocalDateTime.now()

    init {
        code = status.value()
        statusCode = status.name
    }
}