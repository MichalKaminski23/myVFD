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
        summary = "Create vote",
        description = "Creates a vote. If a vote for the (proposal, firefighter) already exists, you may return 409 or update it in service logic (up to you)."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Vote created",
                content = [Content(schema = Schema(implementation = VoteDtos.VoteResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
            ApiResponse(
                responseCode = "409",
                description = "Vote already exists for (proposal, firefighter)",
                content = [Content()]
            )
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createVote(
        @Valid @RequestBody voteDto: VoteDtos.VoteCreate
    ): VoteDtos.VoteResponse =
        voteService.createVote(voteDto)

    @Operation(
        summary = "List votes (paged)",
        description = """
            Returns a paginated list of votes.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: voteId,desc)
            - optional filters (if supported by service): proposalId, firefighterId
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping
    fun getAllVotes(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "voteId,desc") sort: String
    ): PageResponse<VoteDtos.VoteResponse> =
        voteService.getAllVotes(page, size, sort)

    @Operation(
        summary = "Get vote by ID",
        description = "Returns a single vote by `voteId`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Vote found",
                content = [Content(schema = Schema(implementation = VoteDtos.VoteResponse::class))]
            ),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping("/{voteId}")
    fun getVoteById(
        @PathVariable voteId: Int
    ): VoteDtos.VoteResponse =
        voteService.getVoteById(voteId)

    @Operation(
        summary = "Update vote",
        description = "Partially updates an existing vote (e.g., change value). Only non-null fields from body are applied."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Vote updated",
                content = [Content(schema = Schema(implementation = VoteDtos.VoteResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PatchMapping("/{voteId}")
    fun updateVote(
        @PathVariable voteId: Int,
        @Valid @RequestBody voteDto: VoteDtos.VotePatch
    ): VoteDtos.VoteResponse =
        voteService.updateVote(voteId, voteDto)
}