package com.vfd.server.shared

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

object PaginationUtils {

    fun toPageRequest(
        page: Int?,
        size: Int?,
        sort: String?,
        allowedFields: Set<String>,
        defaultSort: String = "id,asc",
        maxSize: Int = 200
    ): PageRequest {
        val safePage = (page ?: 0).coerceAtLeast(0)
        val safeSize = (size ?: 20).coerceIn(1, maxSize)

        val (rawProp, rawDir) = (sort ?: defaultSort).split(",").let {
            it[0] to (it.getOrNull(1) ?: "asc")
        }

        val defaultProp = defaultSort.substringBefore(",")
        val prop = if (rawProp in allowedFields) rawProp else defaultProp
        val dir = if (rawDir.equals("desc", true)) Sort.Direction.DESC else Sort.Direction.ASC

        return PageRequest.of(safePage, safeSize, Sort.by(dir, prop))
    }
}