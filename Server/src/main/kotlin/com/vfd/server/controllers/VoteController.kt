package com.vfd.server.controllers

import com.vfd.server.dtos.VoteDtos
import com.vfd.server.services.VoteService
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

@Tag(name = "Votes", description = "Voting on investment proposals.")
@Validated
@RestController
@RequestMapping("/api/votes")
class VoteController(
    private val voteService: VoteService
) {

    @Operation(
        summary = "Create a new vote for firedepartment's investment proposal",
        description = "Creates a new vote associated with the firedepartment of the currently authenticated user and returns the created vote details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "Vote successfully created",
                content = [Content(schema = Schema(implementation = VoteDtos.VoteResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PostMapping("/my")
    @ResponseStatus(HttpStatus.CREATED)
    fun createVote(
        @AuthenticationPrincipal principal: UserDetails,
        @Valid @RequestBody voteDto: VoteDtos.VoteCreate
    ): VoteDtos.VoteResponse =
        voteService.createVote(principal.username, voteDto)

    @Operation(
        summary = "Get votes from my firedepartment",
        description = """
            Retrieves all votes associated with the firedepartment of the currently authenticated user.

            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: voteId,asc) e.g. `voteId,asc`
            - `investmentProposalId` (required): ID of the investment proposal to filter votes
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Votes retrieved successfully",
                content = [Content(schema = Schema(implementation = VoteDtos.VoteResponse::class))]
            ),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @GetMapping("/my")
    fun getVotes(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "voteId,asc") sort: String,
        @RequestParam investmentProposalId: Int,
        @AuthenticationPrincipal principal: UserDetails
    ): PageResponse<VoteDtos.VoteResponse> =
        voteService.getVotes(page, size, sort, investmentProposalId, principal.username)

    @Operation(
        summary = "Update vote from my firedepartment",
        description = """
            Partially updates an existing vote identified by `voteId`.
            Only non-null fields in the request body will be updated.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Vote updated successfully",
                content = [Content(schema = Schema(implementation = VoteDtos.VoteResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PatchMapping("/my/{voteId}")
    fun updateVote(
        @AuthenticationPrincipal principal: UserDetails,
        @PathVariable voteId: Int,
        @Valid @RequestBody voteDto: VoteDtos.VotePatch
    ): VoteDtos.VoteResponse =
        voteService.updateVote(principal.username, voteId, voteDto)
}