package com.vfd.server.controllers

import com.vfd.server.dtos.UserDtos
import com.vfd.server.services.UserService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Users", description = "Read & update users.")
@Validated
@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @Operation(
        summary = "List users (paged)",
        description = """
            Returns a paginated list of users.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: createdAt,desc) e.g. `lastName,asc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping
    fun getAllUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "createdAt,asc") sort: String
    ): PageResponse<UserDtos.UserResponse> =
        userService.getAllUsers(page, size, sort)

    @Operation(
        summary = "Get user by ID",
        description = "Returns a single user by `userId`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "User found",
                content = [Content(schema = Schema(implementation = UserDtos.UserResponse::class))]
            ),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping("/{userId}")
    fun getUserById(
        @PathVariable userId: Int
    ): UserDtos.UserResponse =
        userService.getUserById(userId)

    @Operation(
        summary = "Update user",
        description = "Partially updates an existing user. Only non-null fields are applied."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "User updated",
                content = [Content(schema = Schema(implementation = UserDtos.UserResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PatchMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: Int,
        @Valid @RequestBody userPatchDto: UserDtos.UserPatch
    ): UserDtos.UserResponse =
        userService.updateUser(userId, userPatchDto)
}