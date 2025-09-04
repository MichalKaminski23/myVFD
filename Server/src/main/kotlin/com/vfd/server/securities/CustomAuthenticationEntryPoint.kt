package com.vfd.server.securities

import com.fasterxml.jackson.databind.ObjectMapper
import com.vfd.server.shared.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val errorResponse = ErrorResponse(
            status = HttpServletResponse.SC_UNAUTHORIZED,
            error = "Unauthorized",
            message = authException.message ?: "Missing or invalid token.",
            path = request.requestURI,
            timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString()
        )

        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}