package com.vfd.server.controllers

import com.vfd.server.exceptions.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException, req: WebRequest): ResponseEntity<ErrorResponse> {
        val err = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = ex.message ?: "Resource not found",
            path = req.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(err, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun handleAll(ex: Exception, req: WebRequest): ResponseEntity<ErrorResponse> {
        val err = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = "Internal server error: ${ex.message ?: "An unexpected error occurred."}",
            path = req.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(err, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

data class ErrorResponse(
    val status: Int,
    val message: String,
    val path: String
)