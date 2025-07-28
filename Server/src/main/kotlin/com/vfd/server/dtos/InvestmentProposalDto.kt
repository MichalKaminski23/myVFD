package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(description = "DTO representing an investment proposal with its related votes.")
data class InvestmentProposalDto(
    @Schema(
        description = "Unique identifier of the investment proposal.",
        example = "100",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val proposalId: Int? = null,

    @Schema(description = "ID of the fire department submitting the proposal.", example = "1")
    val firedepartmentId: Int,

    @Schema(description = "Description of the proposal.", example = "Purchase of new helmets.")
    @field:NotBlank(message = "Proposal description must not be blank.")
    @field:Size(max = 512, message = "Proposal description must be at most 512 characters.")
    val description: String,

    @Schema(description = "Requested investment amount.", example = "5000.00")
    @field:NotNull(message = "Amount must not be null.")
    @field:DecimalMin(value = "0.01", message = "Amount must be greater than zero.")
    val amount: BigDecimal,

    @Schema(description = "Date of submission.", example = "2025-07-28T14:00:00")
    @field:NotNull(message = "Submission date must not be null.")
    val submissionDate: LocalDateTime,

    @Schema(description = "Current status of the proposal (e.g., PENDING, APPROVED).", example = "PENDING")
    @field:NotBlank(message = "Proposal status must not be blank.")
    @field:Size(max = 16, message = "Status must be at most 16 characters.")
    val status: String,

    @Schema(description = "List of votes related to the proposal.")
    @field:Valid
    val votes: List<VoteDto> = emptyList()
)
