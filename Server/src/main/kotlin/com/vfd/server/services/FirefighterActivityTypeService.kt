package com.vfd.server.services

import com.vfd.server.dtos.FirefighterActivityTypeDtos
import org.springframework.data.domain.Page

interface FirefighterActivityTypeService {

    fun createFirefighterActivityType(
        firefighterActivityTypeDto: FirefighterActivityTypeDtos.FirefighterActivityTypeCreate
    ): FirefighterActivityTypeDtos.FirefighterActivityTypeResponse

    fun getAllFirefighterActivityTypes(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firefighterActivityType,asc"
    ): Page<FirefighterActivityTypeDtos.FirefighterActivityTypeResponse>

    fun getFirefighterActivityTypeByCode(
        firefighterActivityTypeCode: String
    ): FirefighterActivityTypeDtos.FirefighterActivityTypeResponse

    fun updateFirefighterActivityType(
        firefighterActivityTypeCode: String,
        firefighterActivityTypeDto: FirefighterActivityTypeDtos.FirefighterActivityTypePatch
    ): FirefighterActivityTypeDtos.FirefighterActivityTypeResponse

}