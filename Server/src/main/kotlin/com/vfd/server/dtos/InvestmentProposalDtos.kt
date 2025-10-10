package com.vfd.server.dtos

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDateTime

object InvestmentProposalDtos {

    @Schema(description = "DTO used for creating a new investment proposal")
    data class InvestmentProposalCreate(
        @field:NotBlank(message = "Description must not be blank.")
        @field:Size(max = 512, message = "Description must bet at most 512 characters.")
        @field:Schema(
            description = "Description of the investment proposal",
            example = "Purchase of a new thermal imaging camera"
        )
        val description: String,

        @field:NotNull(message = "Amount must not be null.")
        @field:Schema(description = "Proposed investment amount", example = "24999.99")
        @field:JsonFormat(shape = JsonFormat.Shape.STRING)
        @field:Digits(integer = 12, fraction = 2)
        @field:PositiveOrZero
        val amount: BigDecimal
    )

    @Schema(description = "DTO used for partially updating an investment proposal")
    data class InvestmentProposalPatch(
        @field:Size(max = 512, message = "Description must be at most 512 characters.")
        @field:Schema(
            description = "Description of the investment proposal",
            example = "Purchase of a new thermal imaging camera"
        )
        val description: String? = null,

        @field:Schema(description = "Proposed investment amount", example = "24999.99")
        @field:JsonFormat(shape = JsonFormat.Shape.STRING)
        @field:Digits(integer = 12, fraction = 2)
        @field:PositiveOrZero
        val amount: BigDecimal? = null,

        @Schema(allowableValues = ["PENDING", "APPROVED", "REJECTED", "CANCELLED"])
        @field:Schema(description = "Status of the proposal", example = "APPROVED")
        val status: String? = null
    )

    @Schema(description = "DTO used for returning investment proposal information")
    data class InvestmentProposalResponse(
        @field:Schema(description = "Unique identifier of the investment proposal", example = "7")
        val investmentProposalId: Int,

        @field:Schema(description = "Fire department that submitted the proposal")
        val firedepartmentId: Int,

        @field:Schema(description = "Description of the proposal", example = "Purchase of a new thermal imaging camera")
        val description: String,

        @field:Schema(description = "Proposed amount", example = "24999.99")
        @field:JsonFormat(shape = JsonFormat.Shape.STRING)
        val amount: BigDecimal,

        @field:Schema(description = "Date when the proposal was submitted", example = "2025-08-03T14:30:00")
        val submissionDate: LocalDateTime,

        @Schema(allowableValues = ["PENDING", "APPROVED", "REJECTED", "CANCELLED"])
        @field:Schema(description = "Status of the proposal", example = "APPROVED")
        val status: String,

        @field:Schema(description = "Total number of votes for this proposal", example = "7")
        val votesCount: Int,

        @field:Schema(description = "Number of YES votes for this proposal", example = "9")
        val votesYesCount: Int,

        @field:Schema(description = "Current firefighter's vote: true=YES, false=NO, null=not voted", example = "true")
        val myVote: Boolean? = null
    )

    @Schema(description = "DTO used for creating a new investment proposal for development purposes")
    data class InvestmentProposalCreateDev(
        @field:NotNull(message = "Firedepartment ID must not be null.")
        @field:Schema(description = "ID of the fire department proposing the investment", example = "7")
        val firedepartmentId: Int,

        @field:NotBlank(message = "Description must not be blank.")
        @field:Size(max = 512, message = "Description must bet at most 512 characters.")
        @field:Schema(
            description = "Description of the investment proposal",
            example = "Purchase of a new thermal imaging camera"
        )
        val description: String,

        @field:NotNull(message = "Submission date must not be null.")
        @field:Schema(description = "Date of the submission", example = "2025-08-03T15:00:00")
        val submissionDate: LocalDateTime,

        @field:NotNull(message = "Amount must not be null.")
        @field:Schema(description = "Proposed investment amount", example = "24999.99")
        val amount: BigDecimal
    )
}