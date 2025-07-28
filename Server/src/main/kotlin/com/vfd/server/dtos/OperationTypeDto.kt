package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "DTO representing a type of operation (e.g., FIRE, RESCUE).")
data class OperationTypeDto(
    @Schema(description = "Unique code of the operation type.", example = "FIRE")
    @field:NotBlank(message = "Operation type must not be blank.")
    @field:Size(max = 16, message = "Operation type must be at most 16 characters.")
    val operationType: String,

    @Schema(description = "Display name of the operation type.", example = "Fire suppression")
    @field:NotBlank(message = "Operation type name must not be blank.")
    @field:Size(max = 64, message = "Operation type name must be at most 64 characters.")
    val name: String
)