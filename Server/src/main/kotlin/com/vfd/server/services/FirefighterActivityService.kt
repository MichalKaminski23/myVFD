package com.vfd.server.services

import com.vfd.server.dtos.FirefighterActivityDtos
import com.vfd.server.shared.PageResponse

interface FirefighterActivityService {

    fun createFirefighterActivity(
        emailAddress: String,
        firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreate
    ): FirefighterActivityDtos.FirefighterActivityResponse

    fun getFirefighterActivities(
        page: Int = 0,
        size: Int = 20,
        sort: String = "activityDate,asc",
        emailAddress: String
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse>

    fun updateFirefighterActivity(
        emailAddress: String,
        firefighterActivityId: Int,
        firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityPatch
    ): FirefighterActivityDtos.FirefighterActivityResponse

    fun createFirefighterActivityDev(firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreateDev): FirefighterActivityDtos.FirefighterActivityResponse

    fun getAllFirefighterActivitiesDev(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firefighterActivityId,asc"
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse>

    fun getFirefighterActivityByIdDev(firefighterActivityId: Int): FirefighterActivityDtos.FirefighterActivityResponse

    fun updateFirefighterActivityDev(
        firefighterActivityId: Int,
        firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityPatch
    ): FirefighterActivityDtos.FirefighterActivityResponse
}