package com.vfd.server.services

import com.vfd.server.dtos.FirefighterActivityDtos
import com.vfd.server.shared.PageResponse

interface FirefighterActivityService {

    fun createFirefighterActivity(firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreate): FirefighterActivityDtos.FirefighterActivityResponse

    fun getAllFirefighterActivities(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firefighterActivityId,asc"
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse>

    fun getFirefighterActivityById(firefighterActivityId: Int): FirefighterActivityDtos.FirefighterActivityResponse

    fun updateFirefighterActivity(
        firefighterActivityId: Int,
        firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityPatch
    ): FirefighterActivityDtos.FirefighterActivityResponse
}