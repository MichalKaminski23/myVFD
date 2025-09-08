package com.vfd.server.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class ResourceConflictException(
    entity: String,
    fieldName: String,
    fieldValue: Any
) : RuntimeException("$entity with $fieldName '$fieldValue' already exists.")