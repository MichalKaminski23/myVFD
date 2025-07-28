package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import java.time.LocalDateTime

@Schema(description = "DTO representing information about a rescue/firefighting operation.")
data class OperationDto(
    @Schema(
        description = "Unique identifier of the operation.",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val operationId: Int? = null,

    @Schema(description = "Fire department that carried out the operation.")
    @field:Valid
    val firedepartment: FiredepartmentDto,

    @Schema(description = "Type of operation.", example = "FIRE")
    val operationType: String,

    @Schema(description = "Address where the operation took place.")
    @field:Valid
    val address: AddressDto,

    @Schema(description = "Date of the operation.", example = "2025-07-28T16:00:00")
    val operationDate: LocalDateTime,

    @Schema(
        description = "Detailed description of the operation.",
        example = "Fire in an industrial building, 4 units involved."
    )
    val description: String,

    @Schema(description = "List of firefighters who participated in the operation.")
    val participants: List<FirefighterDto> = emptyList()
)