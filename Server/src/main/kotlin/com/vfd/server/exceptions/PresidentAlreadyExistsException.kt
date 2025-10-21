package com.vfd.server.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class PresidentAlreadyExistsException(
    message: String = "A president already exists for this firedepartment."
) : RuntimeException(message)