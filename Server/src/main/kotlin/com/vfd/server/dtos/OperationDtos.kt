package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

object OperationDtos {

    @Schema(description = "DTO used for creating a new emergency operation")
    data class OperationCreate(
        @field:NotBlank(message = "Operation type must not be blank.")
        @field:Size(max = 16, message = "Operation type must be at most 16 characters.")
        @field:Schema(description = "Type code of the operation", example = "Fire")
        val operationType: String,

        @field:Valid
        @field:Schema(description = "Address of the fire department")
        val address: AddressDtos.AddressCreate,

        @field:NotNull(message = "Operation date must not be null.")
        @field:Schema(description = "Date and time when the operation occurred", example = "2025-08-03T13:30:00")
        val operationDate: LocalDateTime,

        @field:Size(max = 512, message = "Description must be at most 512 characters.")
        @field:Schema(
            description = "Description of the operation",
            example = "Extinguishing residential building fire."
        )
        val description: String,

        @field:Schema(description = "List of firefighter IDs participating in the operation", example = "[12, 15, 18]")
        val participantIds: MutableSet<Int> = linkedSetOf()
    )

    @Schema(description = "DTO used for partially updating an operation")
    data class OperationPatch(
        @field:Schema(description = "Type code of the operation", example = "Fire")
        val operationType: String? = null,

        @field:Valid
        @field:Schema(description = "Firedepartment's address")
        val address: AddressDtos.AddressCreate? = null,

        @field:Schema(description = "Date and time of the operation", example = "2025-09-01T19:00:00")
        val operationDate: LocalDateTime? = null,

        @field:Size(max = 512, message = "Description must be at most 512 characters.")
        @field:Schema(
            description = "Description of the operation",
            example = "Extinguishing residential building fire."
        )
        val description: String? = null,

        @field:Schema(description = "List of firefighter IDs participating in the operation", example = "[12, 15, 18]")
        val participantIds: MutableSet<Int> = linkedSetOf()
    )

    @Schema(description = "DTO used for returning operation information")
    data class OperationResponse(
        @field:Schema(description = "Unique identifier of the operation", example = "7")
        val operationId: Int,

        @field:Schema(description = "Fire department that executed the operation")
        val firedepartmentId: Int,

        @field:Schema(description = "Address where the operation took place")
        val address: AddressDtos.AddressResponse,

        @field:Schema(description = "Type of the operation")
        val operationTypeName: String,

        @field:Schema(description = "Date and time when the operation occurred", example = "2025-08-03T13:30:00")
        val operationDate: LocalDateTime,

        @field:Schema(
            description = "Description of the operation",
            example = "Extinguishing residential building fire."
        )
        val description: String,

        @field:Schema(description = "List of firefighters who participated in the operation")
        val participants: List<FirefighterDtos.FirefighterResponseShort>
    )

    @Schema(description = "DTO used for creating a new emergency operation for development purposes")
    data class OperationCreateDev(
        @field:NotNull(message = "Firedepartment ID must not be null.")
        @field:Schema(description = "ID of the fire department executing the operation", example = "7")
        val firedepartmentId: Int,

        @field:NotBlank(message = "Operation type must not be blank.")
        @field:Size(max = 16, message = "Operation type must be at most 16 characters.")
        @field:Schema(description = "Type code of the operation", example = "Fire")
        val operationType: String,

        @field:Valid
        @field:Schema(description = "Address of the fire department")
        val address: AddressDtos.AddressCreate,

        @field:NotNull(message = "Operation date must not be null.")
        @field:Schema(description = "Date and time when the operation occurred", example = "2025-08-03T13:30:00")
        val operationDate: LocalDateTime,

        @field:Size(max = 512, message = "Description must be at most 512 characters.")
        @field:Schema(
            description = "Description of the operation",
            example = "Extinguishing residential building fire."
        )
        val description: String,

        @field:Schema(description = "List of firefighter IDs participating in the operation", example = "[12, 15, 18]")
        val participantIds: MutableSet<Int> = linkedSetOf()
    )
}