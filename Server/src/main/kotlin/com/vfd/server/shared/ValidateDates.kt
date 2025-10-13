package com.vfd.server.shared

import com.vfd.server.exceptions.InvalidDatesException
import java.time.LocalDateTime


fun validateDates(startDate: LocalDateTime?, expirationDate: LocalDateTime?, label: String) {
    if (startDate != null && expirationDate != null && startDate.isAfter(expirationDate)) {
        throw InvalidDatesException("$label date cannot be after expiration/end date.")
    }

    if (startDate != null && expirationDate != null && startDate.isAfter(LocalDateTime.now())) {
        throw InvalidDatesException("$label date cannot be after today.")
    }
}