package com.vfd.client.ui.components

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.UserDtos

@Composable
fun AppUserCard(user: UserDtos.UserResponse) {
    AppCard(
        "👤 ${user.firstName} ${user.lastName}",
        "📧 ${user.emailAddress}\n📱 ${user.phoneNumber}",
        "🏠 ${user.address.country}, ${user.address.voivodeship}, " +
                "${user.address.street} ${user.address.houseNumber}/${user.address.apartNumber ?: ""} " +
                "${user.address.postalCode} ${user.address.city}",
        null
    )
}