package com.vfd.server.dtos

import com.vfd.server.entities.InvestmentProposalStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
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
        val amount: BigDecimal? = null,

        @field:NotNull(message = "Status must not be null.")
        @field:Schema(description = "Status of the proposal", example = "APPROVED")
        val investmentProposalStatus: InvestmentProposalStatus? = null
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
        val amount: BigDecimal,

        @field:Schema(description = "Date when the proposal was submitted", example = "2025-08-03T14:30:00")
        val submissionDate: LocalDateTime,

        @field:Schema(description = "Status of the proposal", example = "APPROVED")
        val status: InvestmentProposalStatus,

        @field:Schema(description = "Total number of votes for this proposal", example = "7")
        val votesCount: Int
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

        @field:NotNull(message = "Amount must not be null.")
        @field:Schema(description = "Proposed investment amount", example = "24999.99")
        val amount: BigDecimal
    )
}