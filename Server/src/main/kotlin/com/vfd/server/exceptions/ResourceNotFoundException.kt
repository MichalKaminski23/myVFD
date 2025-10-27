package com.vfd.server.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(
    entity: String,
    fieldName: String,
    fieldValue: Any
) : LocalizedException(
    "resource.not_found",
    arrayOf(entity, fieldName, fieldValue),
    "$entity not found with $fieldName : '$fieldValue'"
)