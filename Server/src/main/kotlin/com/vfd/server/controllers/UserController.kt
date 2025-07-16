// src/main/kotlin/com/vfd/server/controller/UserController.kt
package com.vfd.server.controllers

import com.vfd.server.dtos.UserDto
import com.vfd.server.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun all(): List<UserDto> = userService.findAll()

    @GetMapping("/{id}")
    fun one(@PathVariable id: Integer): UserDto = userService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody dto: UserDto): UserDto =
        userService.create(dto)
}
