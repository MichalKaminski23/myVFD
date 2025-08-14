package com.vfd.server.controllers

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
        summary = "Create investment proposal",
        description = "Creates a new investment proposal and returns its details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Investment proposal created",
                content = [Content(schema = Schema(implementation = InvestmentProposalDtos.InvestmentProposalResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInvestmentProposal(
        @Valid @RequestBody investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreate
    ): InvestmentProposalDtos.InvestmentProposalResponse =
        investmentProposalService.createInvestmentProposal(investmentProposalDto)

    @Operation(
        summary = "List investment proposals (paged)",
        description = """
            Returns a paginated list of investment proposals.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: proposalId,desc) e.g. `submissionDate,desc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping
    fun getAllInvestmentProposals(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "investmentProposalId,desc") sort: String
    ): PageResponse<InvestmentProposalDtos.InvestmentProposalResponse> =
        investmentProposalService.getAllInvestmentProposals(page, size, sort)

    @Operation(
        summary = "Get investment proposal by ID",
        description = "Returns a single investment proposal by `proposalId`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Investment proposal found",
                content = [Content(schema = Schema(implementation = InvestmentProposalDtos.InvestmentProposalResponse::class))]
            ),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping("/{investmentProposalId}")
    fun getInvestmentProposalById(
        @PathVariable investmentProposalId: Int
    ): InvestmentProposalDtos.InvestmentProposalResponse =
        investmentProposalService.getInvestmentProposalById(investmentProposalId)

    @Operation(
        summary = "Update investment proposal",
        description = "Partially updates an existing investment proposal. Only non-null fields are applied."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Investment proposal updated",
                content = [Content(schema = Schema(implementation = InvestmentProposalDtos.InvestmentProposalResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PatchMapping("/{investmentProposalId}")
    fun updateInvestmentProposal(
        @PathVariable investmentProposalId: Int,
        @Valid @RequestBody investmentProposalDto: InvestmentProposalDtos.InvestmentProposalPatch
    ): InvestmentProposalDtos.InvestmentProposalResponse =
        investmentProposalService.updateInvestmentProposal(investmentProposalId, investmentProposalDto)
}