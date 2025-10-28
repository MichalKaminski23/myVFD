package com.vfd.server.shared

import com.vfd.server.exceptions.InvalidDatesException
import java.time.LocalDateTime


fun validateDates(startDate: LocalDateTime?, expirationDate: LocalDateTime?) {
    if (startDate != null && expirationDate != null && startDate.isAfter(expirationDate)) {
        throw InvalidDatesException("invalid.dates.text")
    }

    if (startDate != null && expirationDate != null && startDate.isAfter(LocalDateTime.now())) {
        throw InvalidDatesException("invalid.dates.text2")
    }
}