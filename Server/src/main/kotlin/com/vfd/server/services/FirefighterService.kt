package com.vfd.server.services

import com.vfd.server.dtos.FirefighterDtos
import org.springframework.data.domain.Page

interface FirefighterService {

    fun createFirefighter(firefighterDto: FirefighterDtos.FirefighterCreate): FirefighterDtos.FirefighterResponse

    fun getAllFirefighters(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firefighterId,asc"
    ): Page<FirefighterDtos.FirefighterResponse>

    fun getFirefighterById(firefighterId: Int): FirefighterDtos.FirefighterResponse

    fun updateFirefighter(
        firefighterId: Int,
        firefighterDto: FirefighterDtos.FirefighterPatch
    ): FirefighterDtos.FirefighterResponse
}