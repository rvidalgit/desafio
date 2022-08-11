package com.elo7.desafio.config

import com.elo7.desafio.config.exception.ErrorResponse
import com.elo7.desafio.config.exception.ValidationError
import com.elo7.desafio.exception.NotFoundException
import com.elo7.desafio.exception.SpaceProbeCollidedException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ControllerAdviceError : ResponseEntityExceptionHandler() {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        log.error(ex.message, ex)
        val cause = ex.cause
        if (cause is MissingKotlinParameterException) {
            val missingException: MissingKotlinParameterException = cause
            val fieldName = missingException.path.joinToString(separator = ".") { it.fieldName }
            val error = ValidationError(fieldName, "cannot be null or empty")
            return generateErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed", listOf(error))
        }
        return generateErrorResponse(status, ex.message, null)
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleTooManyMainAddressesException(
        ex: NotFoundException,
        request: WebRequest
    ): ResponseEntity<Any> {
        log.error(ex.message, ex)
        return generateErrorResponse(HttpStatus.NOT_FOUND, ex.message, null)
    }

    @ExceptionHandler(value = [SpaceProbeCollidedException::class])
    fun handleSpaceProbeCollidedException(
        ex: SpaceProbeCollidedException,
        request: WebRequest
    ): ResponseEntity<Any> {
        log.error(ex.message, ex)
        return generateErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.message, null)
    }

    @ExceptionHandler(value = [RuntimeException::class])
    fun handleRuntimeException(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        log.error(ex.message, ex)
        return generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.message, null)
    }

    private fun generateErrorResponse(
        status: HttpStatus,
        message: String?,
        errors: List<ValidationError>?
    ): ResponseEntity<Any> {
        return ResponseEntity(ErrorResponse(status, message, errors), status)
    }
}