package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

object OperationTypeDtos {

    @Schema(description = "DTO used for creating a new operation type")
    data class OperationTypeCreate(
        @field:NotBlank(message = "Operation type key must not be blank.")
        @field:Size(max = 16, message = "Operation type key must be at most 16 characters.")
        @field:Schema(description = "Unique code representing the type of operation", example = "Fire")
        val operationType: String,

        @field:NotBlank(message = "Name must not be blank.")
        @field:Size(max = 64, message = "Name must be at most 64 characters.")
        @field:Schema(description = "Human-readable name of the operation type", example = "Firefighting operation")
        val name: String
    )

    @Schema(description = "DTO used for partially updating an existing operation type")
    data class OperationTypePatch(
        @field:Size(max = 64, message = "Name must be at most 64 characters.")
        @field:Schema(description = "Human-readable name of the operation type", example = "Firefighting operation")
        val name: String? = null
    )

    @Schema(description = "DTO used for returning operation type information")
    data class OperationTypeResponse(
        @field:Schema(description = "Unique code of the operation type", example = "Fire")
        val operationType: String,

        @field:Schema(description = "Human-readable name of the operation type", example = "Firefighting operation")
        val name: String
    )
}