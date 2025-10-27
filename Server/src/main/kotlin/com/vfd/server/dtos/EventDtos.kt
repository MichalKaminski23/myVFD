package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

object EventDtos {

    @Schema(description = "DTO used to create a new event")
    data class EventCreate(
        @field:NotBlank(message = "{event.header.not_blank}")
        @field:Size(max = 128, message = "{event.header.size}")
        @field:Schema(description = "Short header/title of the event", example = "Stulecie OSP")
        val header: String,

        @field:Size(max = 1023, message = "{event.description.size}")
        @field:Schema(
            description = "Detailed description of the event",
            example = "100 lat OSP"
        )
        val description: String,

        @field:NotNull(message = "{event.eventDate.not_null}")
        @field:Schema(description = "Date and time of the event", example = "2025-09-01T18:00:00")
        val eventDate: LocalDateTime
    )

    @Schema(description = "DTO used to partially update an event")
    data class EventPatch(
        @field:Size(max = 128, message = "{event.header.size}")
        @field:Schema(description = "Short header/title of the event", example = "Stulecie OSP")
        val header: String? = null,

        @field:Size(max = 1023, message = "{event.description.size}")
        @field:Schema(description = "Detailed description of the event", example = "100 lat OSP")
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

        @field:Schema(description = "Short header/title of the event", example = "Stulecie OSP")
        val header: String,

        @field:Schema(
            description = "Detailed description of the event",
            example = "100 lat OSP"
        )
        val description: String,

        @field:Schema(description = "Date and time of the event", example = "2025-09-01T18:00:00")
        val eventDate: LocalDateTime
    )

    @Schema(description = "DTO used to create a new event for development purposes")
    data class EventCreateDev(
        @field:NotNull(message = "{event.firedepartmentId.not_null}")
        @field:Schema(description = "ID of the firedepartment the event belongs to", example = "7")
        val firedepartmentId: Int,

        @field:NotBlank(message = "{event.header.not_blank}")
        @field:Size(max = 128, message = "{event.header.size}")
        @field:Schema(description = "Short header/title of the event", example = "Stulecie OSP")
        val header: String,

        @field:Size(max = 1023, message = "{event.description.size}")
        @field:Schema(
            description = "Detailed description of the event",
            example = "100 lat OSP"
        )
        val description: String,

        @field:NotNull(message = "{event.eventDate.not_null}")
        @field:Schema(description = "Date and time of the event", example = "2025-09-01T18:00:00")
        val eventDate: LocalDateTime
    )
}