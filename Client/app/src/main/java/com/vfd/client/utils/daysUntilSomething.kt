package com.vfd.client.utils

import kotlinx.datetime.LocalDateTime
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun daysUntilSomething(ourDate: LocalDateTime?): Long {
    if (ourDate == null) return -1

    val today = LocalDate.now()
    val somethingDate = LocalDate.of(ourDate.year, ourDate.monthNumber, ourDate.dayOfMonth)

    return ChronoUnit.DAYS.between(today, somethingDate)
}
