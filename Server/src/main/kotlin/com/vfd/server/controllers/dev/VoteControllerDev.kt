package com.vfd.server.controllers.dev

import com.vfd.server.dtos.VoteDtos
import com.vfd.server.services.VoteService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Votes", description = "Voting on investment proposals. (Development only)")
@Profile("dev")
@Validated
@RestController
@RequestMapping("/api/dev/votes")
class VoteControllerDev(
    private val voteService: VoteService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createVoteDev(
        @Valid @RequestBody voteDto: VoteDtos.VoteCreateDev
    ): VoteDtos.VoteResponse =
        voteService.createVoteDev(voteDto)

    @GetMapping
    fun getAllVotesDev(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "voteId,asc") sort: String
    ): PageResponse<VoteDtos.VoteResponse> =
        voteService.getAllVotesDev(page, size, sort)

    @GetMapping("/{voteId}")
    fun getVoteByIdDev(
        @PathVariable voteId: Int
    ): VoteDtos.VoteResponse =
        voteService.getVoteByIdDev(voteId)

    @PatchMapping("/{voteId}")
    fun updateVoteDev(
        @PathVariable voteId: Int,
        @Valid @RequestBody voteDto: VoteDtos.VotePatch
    ): VoteDtos.VoteResponse =
        voteService.updateVoteDev(voteId, voteDto)
}