package com.vfd.client

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime

class LocalDateTimeAdapter {
    @ToJson
    fun toJson(value: LocalDateTime): String = value.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    @FromJson
    fun fromJson(value: String): LocalDateTime = LocalDateTime.parse(value)
}
