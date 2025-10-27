package com.vfd.server.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class PresidentAlreadyExistsException(emailAddress: String) : LocalizedException(
    "president.already_exists",
    arrayOf(emailAddress),
    "A president already exists for this firedepartment."
)