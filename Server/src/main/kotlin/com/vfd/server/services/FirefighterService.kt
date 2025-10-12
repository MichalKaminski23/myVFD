package com.vfd.server.services

import com.vfd.server.dtos.FirefighterDtos
import com.vfd.server.shared.PageResponse

interface FirefighterService {

    fun createFirefighter(
        emailAddress: String,
        firefighterDto: FirefighterDtos.FirefighterCreate
    ): FirefighterDtos.FirefighterResponse

    fun getFirefighters(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firstName,asc",
        emailAddress: String
    ): PageResponse<FirefighterDtos.FirefighterResponse>

    fun updateFirefighter(
        emailAddress: String,
        firefighterId: Int,
        firefighterDto: FirefighterDtos.FirefighterPatch
    ): FirefighterDtos.FirefighterResponse

    fun getFirefighterByEmailAddress(emailAddress: String): FirefighterDtos.FirefighterResponse

    fun getPendingFirefighters(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firstName,asc",
        emailAddress: String
    ): PageResponse<FirefighterDtos.FirefighterResponse>
    
    fun createFirefighterDev(firefighterDto: FirefighterDtos.FirefighterCreate): FirefighterDtos.FirefighterResponse

    fun getAllFirefightersDev(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firefighterId,asc",
    ): PageResponse<FirefighterDtos.FirefighterResponse>

    fun getFirefighterByIdDev(firefighterId: Int): FirefighterDtos.FirefighterResponse

    fun updateFirefighterDev(
        firefighterId: Int,
        firefighterDto: FirefighterDtos.FirefighterPatch
    ): FirefighterDtos.FirefighterResponse
}