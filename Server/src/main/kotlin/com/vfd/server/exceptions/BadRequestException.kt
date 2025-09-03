//package com.vfd.server.exceptions
//
//import org.springframework.http.HttpStatus
//import org.springframework.web.bind.annotation.ResponseStatus
//
//@ResponseStatus(HttpStatus.BAD_REQUEST)
//class BadRequestException(
//    entity: String,
//    fieldName: String,
//    reason: Any
//) : RuntimeException("$entity's '$fieldName' $reason.")