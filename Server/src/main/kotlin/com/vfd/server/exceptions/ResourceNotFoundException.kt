package com.vfd.server.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(
    entity: String,
    fieldName: String,
    fieldValue: Any
) : RuntimeException("$entity with $fieldName '$fieldValue' not found.")