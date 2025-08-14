package com.vfd.server.controllers

import com.vfd.server.dtos.UserDtos
import com.vfd.server.services.UserService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getAllUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "createdAt,desc") sort: String
    ): Page<UserDtos.UserResponse> =
        userService.getAllUsers(page, size, sort)

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Int): UserDtos.UserResponse =
        userService.getUserById(id)

    @PatchMapping("/{id}")
    fun patchUser(
        @PathVariable id: Int,
        @Valid @RequestBody dto: UserDtos.UserPatch
    ): UserDtos.UserResponse =
        userService.updateUser(id, dto)
}
