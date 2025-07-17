//package com.vfd.server.mappers
//
//import com.vfd.server.dtos.UserLoginDto
//import org.mapstruct.Mapper
//import org.mapstruct.Mapping
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//
//@Mapper(componentModel = "spring")
//interface AuthMapper {
//    @Mapping(source = "emailAddress", target = "principal")
//    @Mapping(source = "password", target = "credentials")
//    fun toAuthToken(dto: UserLoginDto): UsernamePasswordAuthenticationToken
//}