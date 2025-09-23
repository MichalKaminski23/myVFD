package com.vfd.server.controllers.dev

import com.vfd.server.dtos.InvestmentProposalDtos
import com.vfd.server.services.InvestmentProposalService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Investment Proposals", description = "CRUD for investment proposals. (Development only)")
@Profile("dev")
@Validated
@RestController
@RequestMapping("/api/dev/investment-proposals")
class InvestmentProposalControllerDev(
    private val investmentProposalService: InvestmentProposalService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInvestmentProposalDev(
        @Valid @RequestBody investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreateDev
    ): InvestmentProposalDtos.InvestmentProposalResponse =
        investmentProposalService.createInvestmentProposalDev(investmentProposalDto)

    @GetMapping
    fun getAllInvestmentProposalsDev(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "investmentProposalId,asc") sort: String
    ): PageResponse<InvestmentProposalDtos.InvestmentProposalResponse> =
        investmentProposalService.getAllInvestmentProposalsDev(page, size, sort)

    @GetMapping("/{investmentProposalId}")
    fun getInvestmentProposalByIdDev(
        @PathVariable investmentProposalId: Int
    ): InvestmentProposalDtos.InvestmentProposalResponse =
        investmentProposalService.getInvestmentProposalByIdDev(investmentProposalId)

    @PatchMapping("/{investmentProposalId}")
    fun updateInvestmentProposalDev(
        @PathVariable investmentProposalId: Int,
        @Valid @RequestBody investmentProposalDto: InvestmentProposalDtos.InvestmentProposalPatch
    ): InvestmentProposalDtos.InvestmentProposalResponse =
        investmentProposalService.updateInvestmentProposalDev(investmentProposalId, investmentProposalDto)
}