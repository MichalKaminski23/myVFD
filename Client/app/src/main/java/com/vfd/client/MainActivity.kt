package com.vfd.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.vfd.client.ui.screens.UserScreen
import com.vfd.client.ui.theme.MyVFDMobileTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyVFDMobileTheme {
                val navController = rememberNavController()
                UserScreen(navController = navController)
            }
        }
    }
}