package com.vfd.client

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PageResponse<T>(
    val items: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)
