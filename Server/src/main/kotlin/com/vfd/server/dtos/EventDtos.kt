package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

object EventDtos {

    @Schema(description = "DTO used to create a new event")
    data class EventCreate(
        @field:NotNull(message = "Firedepartment ID must not be null.")
        @field:Schema(description = "ID of the firedepartment the event belongs to", example = "7")
        val firedepartmentId: Int,

        @field:NotBlank(message = "Header must not be blank.")
        @field:Size(max = 128, message = "Header must be at most 128 characters.")
        @field:Schema(description = "Short header/title of the event", example = "Potato's Day")
        val header: String,

        @field:Size(max = 1023, message = "Description must be at most 1023 characters.")
        @field:Schema(
            description = "Detailed description of the event",
            example = "The best day in the world."
        )
        val description: String,

        @field:NotNull(message = "Event date must not be null.")
        @field:Schema(description = "Date and time of the event", example = "2025-09-01T18:00:00")
        val eventDate: LocalDateTime
    )


    @Schema(description = "DTO used to partially update an event")
    data class EventPatch(
        @field:Size(max = 128, message = "Header must be at most 128 characters.")
        @field:Schema(description = "Short header/title of the event", example = "Yraining session")
        val header: String? = null,

        @field:Size(max = 1023, message = "Description must be at most 1023 characters.")
        @field:Schema(description = "Detailed description of the event", example = "Description of the training.")
        val description: String? = null,

        @field:Schema(description = "Date and time of the event", example = "2025-09-01T19:00:00")
        val eventDate: LocalDateTime? = null
    )

    @Schema(description = "DTO used to return event information")
    data class EventResponse(
        @field:Schema(description = "ID of the event", example = "10")
        val eventId: Int,

        @field:Schema(description = "ID of the firedepartment the event belongs to", example = "1")
        val firedepartmentId: Int,

        @field:Schema(description = "Short header/title of the event", example = "Training for new members")
        val header: String,

        @field:Schema(
            description = "Detailed description of the event",
            example = "Introduction training for the new volunteers."
        )
        val description: String,

        @field:Schema(description = "Date and time of the event", example = "2025-09-01T18:00:00")
        val eventDate: LocalDateTime
    )
}