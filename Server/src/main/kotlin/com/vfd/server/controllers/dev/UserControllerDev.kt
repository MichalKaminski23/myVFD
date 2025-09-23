package com.vfd.server.controllers.dev

import com.vfd.server.dtos.UserDtos
import com.vfd.server.services.UserService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Users", description = "CRUD for users - create in Auth. (Development only)")
@Profile("dev")
@Validated
@RestController
@RequestMapping("/api/dev/users")
class UserControllerDev(
    private val userService: UserService
) {

    @GetMapping
    fun getAllUsersDev(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "userId,asc") sort: String
    ): PageResponse<UserDtos.UserResponse> =
        userService.getAllUsersDev(page, size, sort)

    @GetMapping("/{userId}")
    fun getUserByIdDev(
        @PathVariable userId: Int
    ): UserDtos.UserResponse =
        userService.getUserByIdDev(userId)

    @PatchMapping("/{userId}")
    fun updateUserDev(
        @PathVariable userId: Int,
        @Valid @RequestBody userDto: UserDtos.UserPatch
    ): UserDtos.UserResponse =
        userService.updateUserDev(userId, userDto)
}