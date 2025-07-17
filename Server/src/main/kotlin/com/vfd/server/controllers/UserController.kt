//package com.vfd.server.controllers
//
//import com.vfd.server.dtos.UserInfoDto
//import com.vfd.server.dtos.UserRegistrationDto
//import com.vfd.server.services.UserService
//import jakarta.validation.Valid
//import org.springframework.http.HttpStatus
//import org.springframework.web.bind.annotation.*
//
//@RestController
//@RequestMapping("/api/users")
//class UserController(private val userService: UserService) {
//
//    @GetMapping
//    fun all(): List<UserInfoDto> = userService.findAll()
//
//    @GetMapping("/{id}")
//    fun one(@PathVariable id: Int): UserInfoDto = userService.findById(id)
//
//    @PostMapping("/register")
//    @ResponseStatus(HttpStatus.CREATED)
//    fun register(@Valid @RequestBody dto: UserRegistrationDto): UserInfoDto =
//        userService.register(dto)
//}