package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Schema(description = "DTO representing a vote made by a firefighter on an investment proposal.")
data class VoteDto(
    @Schema(description = "Unique identifier of the vote.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    val voteId: Int? = null,

    @Schema(description = "ID of the related investment proposal.", example = "1")
    val proposalId: Int,

    @Schema(description = "ID of the firefighter who voted.", example = "1")
    val firefighterId: Int,

    @Schema(description = "Value of the vote (true = YES, false = NO).", example = "true")
    @field:NotNull(message = "Vote value must not be null.")
    val voteValue: Boolean,

    @Schema(description = "Date when the vote was cast.", example = "2025-07-28T15:30:00")
    @field:NotNull(message = "Vote date must not be null.")
    val voteDate: LocalDateTime
)
