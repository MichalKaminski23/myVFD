package com.vfd.server.services

import com.vfd.server.dtos.FirefighterActivityDtos
import org.springframework.data.domain.Page

interface FirefighterActivityService {

    fun createFirefighterActivity(firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreate): FirefighterActivityDtos.FirefighterActivityResponse

    fun getAllFirefighterActivities(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firefighterActivityId,asc"
    ): Page<FirefighterActivityDtos.FirefighterActivityResponse>

    fun getFirefighterActivityById(firefighterActivityId: Int): FirefighterActivityDtos.FirefighterActivityResponse

    fun updateFirefighterActivity(
        firefighterActivityId: Int,
        firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityPatch
    ): FirefighterActivityDtos.FirefighterActivityResponse
}