package com.vfd.server.securities

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val customUserDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {

        if (request.requestURI.startsWith("/api/auth/")) {
            chain.doFilter(request, response)
            return
        }

        val header = request.getHeader("Authorization")

        if (header?.startsWith("Bearer ") == true) {
            val token = header.removePrefix("Bearer ").trim()

            if (jwtTokenProvider.validateToken(token)) {
                val emailAddress = jwtTokenProvider.getEmailAddressFromToken(token)
                val userDetails = customUserDetailsService.loadUserByUsername(emailAddress)

                val auth = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = auth
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token.")
                return
            }
        }
        chain.doFilter(request, response)
    }
}