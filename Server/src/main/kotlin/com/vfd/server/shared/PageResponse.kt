package com.vfd.server.shared

data class PageResponse<T>(
    val items: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)

fun <T> org.springframework.data.domain.Page<T>.toPageResponse(): PageResponse<T> = PageResponse(
    items = content,
    page = number,
    size = size,
    totalElements = totalElements,
    totalPages = totalPages,
    hasNext = hasNext(),
    hasPrevious = hasPrevious()
)