package com.vfd.server.controllers

import com.vfd.server.dtos.UserInfoDto
import com.vfd.server.services.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUsers(): List<UserInfoDto> = userService.getAllUsers()

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Int): UserInfoDto = userService.getUserById(id)

}