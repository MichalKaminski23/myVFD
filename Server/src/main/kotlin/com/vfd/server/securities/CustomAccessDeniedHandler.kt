package com.vfd.server.securities

import com.fasterxml.jackson.databind.ObjectMapper
import com.vfd.server.shared.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.MessageSource
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class CustomAccessDeniedHandler(
    private val objectMapper: ObjectMapper,
    private val messageSource: MessageSource
) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        val locale = request.locale
        val message = messageSource.getMessage(
            "access.denied",
            null,
            "You do not have permission to access this resource.",
            locale
        )

        val errorResponse = ErrorResponse(
            status = HttpServletResponse.SC_FORBIDDEN,
            error = "Forbidden",
            message = message!!,
            path = request.requestURI,
            timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString()
        )

        response.status = HttpServletResponse.SC_FORBIDDEN
        response.contentType = "application/json"
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}