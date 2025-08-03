package com.vfd.server.controllers

import com.vfd.server.dtos.UserDtos
import com.vfd.server.services.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Users", description = "Endpoints for managing users")
@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @Operation(summary = "Get all users")
    @GetMapping
    fun getAllUsers(): List<UserDtos.UserResponse> = userService.getAllUsers()

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    fun getUserById(
        @Parameter(description = "ID of the user to retrieve", example = "101")
        @PathVariable id: Int
    ): UserDtos.UserResponse = userService.getUserById(id)

    @Operation(summary = "Update user data")
    @PatchMapping("/{id}")
    fun updateUser(
        @Parameter(description = "ID of the user to update", example = "101")
        @PathVariable id: Int,
        @Valid @RequestBody dto: UserDtos.UserPatch
    ): UserDtos.UserResponse = userService.updateUser(id, dto)

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(
        @Parameter(description = "ID of the user to delete", example = "101")
        @PathVariable id: Int
    ) {
        userService.deleteUser(id)
    }
}
