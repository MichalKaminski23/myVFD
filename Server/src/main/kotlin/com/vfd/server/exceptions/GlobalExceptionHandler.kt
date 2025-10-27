package com.vfd.server.exceptions

import com.vfd.server.shared.ErrorResponse
import com.vfd.server.shared.ValidationErrorResponse
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.util.*

@ControllerAdvice
class GlobalExceptionHandler(private val messageSource: MessageSource) {

    private fun resolveMessage(exception: Exception, locale: Locale, defaultMsg: String): String? {
        return if (exception is LocalizedException) {
            val localizedArgs: Array<Any>? = exception.args
                ?.map { arg ->
                    messageSource.getMessage(arg.toString(), null, arg.toString(), locale) as Any
                }
                ?.toTypedArray()
            messageSource.getMessage(exception.messageKey, localizedArgs, defaultMsg, locale)
        } else {
            val keyOrDefault = exception.message ?: defaultMsg
            messageSource.getMessage(keyOrDefault, null, keyOrDefault, locale)
        }
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(
        exception: ResourceNotFoundException,
        request: WebRequest,
        locale: Locale
    ): ResponseEntity<ErrorResponse> {
        val message = resolveMessage(exception, locale, "Resource not found")
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Not Found",
            message = message!!,
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ResourceConflictException::class)
    fun handleConflict(
        exception: ResourceConflictException,
        request: WebRequest,
        locale: Locale
    ): ResponseEntity<ErrorResponse> {
        val message = resolveMessage(exception, locale, "Resource conflict")
        val error = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Conflict",
            message = message!!,
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        exception: MethodArgumentNotValidException,
        request: WebRequest,
        locale: Locale
    ): ResponseEntity<ValidationErrorResponse> {
        val errors = exception.bindingResult.fieldErrors.associate { fieldError ->
            fieldError.field to (try {
                messageSource.getMessage(fieldError, locale)
            } catch (_: Exception) {
                fieldError.defaultMessage ?: "Invalid value"
            })
        }

        val errorResponse = ValidationErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            messages = errors,
            path = request.getDescription(false).replace("uri=", "")
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidPasswordException::class)
    fun handleInvalidPassword(
        exception: InvalidPasswordException,
        request: WebRequest,
        locale: Locale
    ): ResponseEntity<ErrorResponse> {
        val message = resolveMessage(exception, locale, "Invalid password")
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = message!!,
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidDatesException::class)
    fun handleInvalidDates(
        exception: InvalidDatesException,
        request: WebRequest,
        locale: Locale
    ): ResponseEntity<ErrorResponse> {
        val message = resolveMessage(exception, locale, "Invalid date range")
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = message!!,
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidNumberException::class)
    fun handleInvalidNumber(
        exception: InvalidNumberException,
        request: WebRequest,
        locale: Locale
    ): ResponseEntity<ErrorResponse> {
        val message = resolveMessage(exception, locale, "Invalid number")
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = message!!,
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(PresidentAlreadyExistsException::class)
    fun handlePresidentAlreadyExists(
        exception: PresidentAlreadyExistsException,
        request: WebRequest,
        locale: Locale
    ): ResponseEntity<ErrorResponse> {
        val message = resolveMessage(exception, locale, "A president already exists for this firedepartment.")
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = message!!,
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(
        exception: BadCredentialsException,
        request: WebRequest,
        locale: Locale
    ): ResponseEntity<ErrorResponse> {
        val message = resolveMessage(exception, locale, "Invalid password")
        val error = ErrorResponse(
            status = HttpStatus.UNAUTHORIZED.value(),
            error = "Unauthorized",
            message = message!!,
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbidden(
        exception: ForbiddenException,
        request: WebRequest,
        locale: Locale
    ): ResponseEntity<ErrorResponse> {
        val message = resolveMessage(exception, locale, "Access is forbidden")
        val error = ErrorResponse(
            status = HttpStatus.FORBIDDEN.value(),
            error = "Forbidden",
            message = message!!,
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(InvalidStatusException::class)
    fun handleInvalidStatus(
        exception: InvalidStatusException,
        request: WebRequest,
        locale: Locale
    ): ResponseEntity<ErrorResponse> {
        val message = resolveMessage(exception, locale, "Invalid value")
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = message!!,
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleAll(exception: Exception, request: WebRequest, locale: Locale): ResponseEntity<ErrorResponse> {
        if (exception is AccessDeniedException ||
            exception is AuthorizationDeniedException
        ) {
            throw exception
        }
        val message = resolveMessage(exception, locale, "An unexpected error occurred.")
        val error = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = "Internal server error: $message",
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}