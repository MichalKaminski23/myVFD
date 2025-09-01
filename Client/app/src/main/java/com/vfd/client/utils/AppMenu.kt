package com.vfd.client.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place

object AppMenu {
    val items = listOf(
        MenuItem("Users", Icons.Filled.Person, "users"),
        MenuItem("Firedepartments", Icons.Filled.Place, "firedepartments"),
        MenuItem("Dashboard", Icons.Filled.Home, "dashboard")
    )
}