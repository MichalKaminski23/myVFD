package com.vfd.server.controllers

import com.vfd.server.dtos.UserDtos
import com.vfd.server.services.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Users", description = "CRUD for users - create in Auth.")
@Validated
@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @Operation(
        summary = "Update user",
        description = """
            Partially updates an existing user identified by `userId`.
            Only non-null fields in the request body will be updated.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "User updated",
                content = [Content(schema = Schema(implementation = UserDtos.UserResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "409", ref = "Conflict"),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me")
    fun updateUser(
        @AuthenticationPrincipal principal: UserDetails,
        @Valid @RequestBody userDto: UserDtos.UserPatch
    ): UserDtos.UserResponse =
        userService.updateUser(principal.username, userDto)

    @Operation(
        summary = "Get current user",
        description = "Returns the currently authenticated user based on the provided JWT token."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Current user retrieved",
                content = [Content(schema = Schema(implementation = UserDtos.UserResponse::class))]
            ),
            ApiResponse(responseCode = "401", ref = "Unauthorized"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    fun getUserByEmailAddress(@AuthenticationPrincipal principal: UserDetails): UserDtos.UserResponse {
        return userService.getUserByEmailAddress(principal.username)
    }
}