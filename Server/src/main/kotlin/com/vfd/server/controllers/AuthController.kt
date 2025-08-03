package com.vfd.server.controllers

import com.vfd.server.dtos.AuthResponseDto
import com.vfd.server.dtos.UserDtos
import com.vfd.server.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns JWT token.")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody dto: UserDtos.UserCreate): AuthResponseDto =
        authService.register(dto)

    @Operation(summary = "Authenticate user", description = "Verifies credentials and returns JWT token.")
    @PostMapping("/login")
    fun login(@Valid @RequestBody dto: UserDtos.UserLogin): AuthResponseDto =
        authService.login(dto)
}
