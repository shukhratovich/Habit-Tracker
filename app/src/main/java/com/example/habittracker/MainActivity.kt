package com.example.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.habittracker.core.ui.theme.RoutinerTheme
import com.example.habittracker.feature.home.HomeScreenTest
import com.example.habittracker.feature.home.RoutinerHomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoutinerTheme {
                RoutinerHomeScreen()
            }
        }
    }
}