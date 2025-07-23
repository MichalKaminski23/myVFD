// src/main/kotlin/com/vfd/server/controllers/AuthController.kt
package com.vfd.server.controllers

import com.vfd.server.dtos.AuthResponseDto
import com.vfd.server.dtos.UserLoginDto
import com.vfd.server.dtos.UserRegistrationDto
import com.vfd.server.services.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody dto: UserRegistrationDto): AuthResponseDto =
        authService.register(dto)

    @PostMapping("/login")
    fun login(@Valid @RequestBody dto: UserLoginDto): AuthResponseDto =
        authService.login(dto)
}
