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
        @field:NotBlank(message = "{operation.operationType.not_blank}")
        @field:Size(max = 16, message = "{operation.operationType.size}")
        @field:Schema(description = "Type code of the operation", example = "POZ")
        val operationType: String,

        @field:Valid
        @field:Schema(description = "Address of the fire department")
        val address: AddressDtos.AddressCreate,

        @field:NotNull(message = "{operation.operationDate.not_null}")
        @field:Schema(description = "Date and time when the operation occurred", example = "2025-08-03T13:30:00")
        val operationDate: LocalDateTime,

        @field:NotNull(message = "{operation.operationEnd.not_null}")
        @field:Schema(description = "Date and time when the operation ended", example = "2025-08-03T15:05:00")
        val operationEnd: LocalDateTime,

        @field:Size(max = 512, message = "{operation.description.size}")
        @field:Schema(
            description = "Description of the operation",
            example = "Gaszenie pozaru budynku mieszkalnego."
        )
        val description: String,

        @field:Schema(description = "List of firefighter IDs participating in the operation", example = "[1, 2, 6]")
        val participantsIds: Set<Int>
    )

    @Schema(description = "DTO used for partially updating an operation")
    data class OperationPatch(
        @field:Schema(description = "Type code of the operation", example = "POZ")
        val operationType: String? = null,

        @field:Valid
        @field:Schema(description = "Firedepartment's address")
        val address: AddressDtos.AddressCreate? = null,

        @field:Schema(description = "Date and time of the operation", example = "2025-09-01T19:00:00")
        val operationDate: LocalDateTime? = null,

        @field:Schema(description = "Date and time when the operation ended", example = "2025-08-03T15:05:00")
        val operationEnd: LocalDateTime? = null,

        @field:Size(max = 512, message = "{operation.description.size}")
        @field:Schema(
            description = "Description of the operation",
            example = "Gaszeenie pozaru budynku mieszkalnego."
        )
        val description: String? = null,

        @field:Schema(description = "List of firefighter IDs participating in the operation", example = "[1, 2, 6]")
        val participantsIds: Set<Int>? = null
    )

    @Schema(description = "DTO used for returning operation information")
    data class OperationResponse(
        @field:Schema(description = "Unique identifier of the operation", example = "7")
        val operationId: Int,

        @field:Schema(description = "Fire department that executed the operation", example = "3")
        val firedepartmentId: Int,

        @field:Schema(description = "Address where the operation took place")
        val address: AddressDtos.AddressResponse,

        @field:Schema(description = "Type of the operation", example = "POZ")
        val operationTypeName: String,

        @field:Schema(description = "Date and time when the operation occurred", example = "2025-08-03T13:30:00")
        val operationDate: LocalDateTime,

        @field:Schema(description = "Date and time when the operation ended", example = "2025-08-03T15:05:00")
        val operationEnd: LocalDateTime,

        @field:Schema(
            description = "Description of the operation",
            example = "Gaszenie pozaru budynku mieszkalnego."
        )
        val description: String,

        @field:Schema(description = "List of firefighters who participated in the operation", example = "[1, 2, 6]")
        val participants: List<FirefighterDtos.FirefighterResponseShort>
    )

    @Schema(description = "DTO used for creating a new emergency operation for development purposes")
    data class OperationCreateDev(
        @field:NotNull(message = "{operation.firedepartmentId.not_null}")
        @field:Schema(description = "ID of the fire department executing the operation", example = "7")
        val firedepartmentId: Int,

        @field:NotBlank(message = "{operation.operationType.not_blank}")
        @field:Size(max = 16, message = "{operation.operationType.size}")
        @field:Schema(description = "Type code of the operation", example = "POZ")
        val operationType: String,

        @field:Valid
        @field:Schema(description = "Address of the fire department")
        val address: AddressDtos.AddressCreate,

        @field:NotNull(message = "{operation.operationDate.not_null}")
        @field:Schema(description = "Date and time when the operation occurred", example = "2025-08-03T13:30:00")
        val operationDate: LocalDateTime,

        @field:NotNull(message = "{operation.operationEnd.not_null}")
        @field:Schema(description = "Date and time when the operation ended", example = "2025-08-03T15:05:00")
        val operationEnd: LocalDateTime,

        @field:Size(max = 512, message = "{operation.description.size}")
        @field:Schema(
            description = "Description of the operation",
            example = "Gaszenie pozaru budynku mieszkalnego."
        )
        val description: String,

        @field:Schema(description = "List of firefighter IDs participating in the operation", example = "[1, 2, 7]")
        val participantIds: MutableSet<Int> = linkedSetOf()
    )
}