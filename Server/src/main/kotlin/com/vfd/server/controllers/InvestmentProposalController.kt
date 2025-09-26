package com.vfd.server.controllers

import com.vfd.server.dtos.AssetDtos
import com.vfd.server.dtos.InvestmentProposalDtos
import com.vfd.server.services.InvestmentProposalService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Investment Proposals", description = "CRUD for investment proposals.")
@Validated
@RestController
@RequestMapping("/api/investment-proposals")
class InvestmentProposalController(
    private val investmentProposalService: InvestmentProposalService
) {

    @Operation(
        summary = "Create a new investment proposal for my firedepartment",
        description = "Creates a new investment proposal associated with the firedepartment of the currently authenticated user and returns the created asset details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "Investment proposal successfully created",
                content = [Content(schema = Schema(implementation = InvestmentProposalDtos.InvestmentProposalResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PostMapping("/my")
    @ResponseStatus(HttpStatus.CREATED)
    fun createInvestmentProposal(
        @AuthenticationPrincipal principal: UserDetails,
        @Valid @RequestBody investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreate
    ): InvestmentProposalDtos.InvestmentProposalResponse =
        investmentProposalService.createInvestmentProposal(principal.username, investmentProposalDto)

    @Operation(
        summary = "Get investment proposals from my firedepartment",
        description = """
            Retrieves all investment proposals associated with the firedepartment of the currently authenticated user.

            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: investmentProposalId,asc) e.g. `investmentProposalId,asc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Investment proposals retrieved successfully",
                content = [Content(schema = Schema(implementation = AssetDtos.AssetResponse::class))]
            ),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @GetMapping("/my")
    fun getInvestmentProposals(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "submissionDate,desc") sort: String,
        @AuthenticationPrincipal principal: UserDetails
    ): PageResponse<InvestmentProposalDtos.InvestmentProposalResponse> =
        investmentProposalService.getInvestmentProposals(page, size, sort, principal.username)

    @Operation(
        summary = "Update investment proposal from my firedepartment",
        description = """
            Partially updates an existing investment proposal identified by `investmentProposalId`.
            Only non-null fields in the request body will be updated.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Investment proposal updated successfully",
                content = [Content(schema = Schema(implementation = InvestmentProposalDtos.InvestmentProposalResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PatchMapping("/my/{investmentProposalId}")
    fun updateInvestmentProposal(
        @AuthenticationPrincipal principal: UserDetails,
        @PathVariable investmentProposalId: Int,
        @Valid @RequestBody investmentProposalDto: InvestmentProposalDtos.InvestmentProposalPatch
    ): InvestmentProposalDtos.InvestmentProposalResponse =
        investmentProposalService.updateInvestmentProposal(
            principal.username,
            investmentProposalId,
            investmentProposalDto
        )
}