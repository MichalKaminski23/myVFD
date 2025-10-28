package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

object OperationTypeDtos {

    @Schema(description = "DTO used for creating a new operation type")
    data class OperationTypeCreate(
        @field:NotBlank(message = "{operationType.key.not_blank}")
        @field:Size(max = 16, message = "{operationType.key.size}")
        @field:Schema(description = "Unique code representing the type of operation", example = "MAGAZ")
        val operationType: String,

        @field:NotBlank(message = "{operationType.name.not_blank}")
        @field:Size(max = 64, message = "{operationType.name.size}")
        @field:Schema(description = "Human-readable name of the operation type", example = "Czujka w magazynie")
        val name: String
    )

    @Schema(description = "DTO used for partially updating an existing operation type")
    data class OperationTypePatch(
        @field:Size(max = 64, message = "{operationType.name.size}")
        @field:Schema(description = "Human-readable name of the operation type", example = "Czujka w magazynie")
        val name: String? = null
    )

    @Schema(description = "DTO used for returning operation type information")
    data class OperationTypeResponse(
        @field:Schema(description = "Unique code of the operation type", example = "MAGAZ")
        val operationType: String,

        @field:Schema(description = "Human-readable name of the operation type", example = "Czujka w magazynie")
        val name: String
    )
}