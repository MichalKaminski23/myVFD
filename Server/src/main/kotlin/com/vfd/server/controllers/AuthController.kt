package com.vfd.server.controllers

import com.vfd.server.dtos.AuthResponseDto
import com.vfd.server.dtos.UserDtos
import com.vfd.server.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Auth", description = "Endpoints for user authentication, registration and logout.")
@Validated
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account and returns JWT token."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "User registered",
                content = [Content(schema = Schema(implementation = AuthResponseDto::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "409", ref = "Conflict")
        ]
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(
        @Valid @RequestBody userDto: UserDtos.UserCreate
    ): AuthResponseDto = authService.register(userDto)

    @Operation(
        summary = "Authenticate user",
        description = "Verifies credentials and returns JWT token."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Authenticated",
                content = [Content(schema = Schema(implementation = AuthResponseDto::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "401", ref = "Unauthorized")
        ]
    )
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody userDto: UserDtos.UserLogin
    ): AuthResponseDto = authService.login(userDto)
}