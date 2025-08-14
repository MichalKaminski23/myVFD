package com.vfd.server.services

import com.vfd.server.dtos.FirefighterDtos
import com.vfd.server.shared.PageResponse

interface FirefighterService {

    fun createFirefighter(firefighterDto: FirefighterDtos.FirefighterCreate): FirefighterDtos.FirefighterResponse

    fun getAllFirefighters(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firefighterId,asc"
    ): PageResponse<FirefighterDtos.FirefighterResponse>

    fun getFirefighterById(firefighterId: Int): FirefighterDtos.FirefighterResponse

    fun updateFirefighter(
        firefighterId: Int,
        firefighterDto: FirefighterDtos.FirefighterPatch
    ): FirefighterDtos.FirefighterResponse
}