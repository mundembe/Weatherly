package com.example.weatherly.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.weatherly.ui.theme.WeatherlyTheme
import com.example.weatherly.viewmodel.HomeViewModel
import com.example.weatherly.viewmodel.WeatherViewModel
import com.example.weatherly.LoginActivity
import com.example.weatherly.viewmodel.WeatherViewModelFactory

class HomeActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    // FIX: Use the factory to create the WeatherViewModel
    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherlyTheme {
                val uiState by homeViewModel.uiState.collectAsState()

                if (uiState.currentUser == null) {
                    // Redirect if not logged in
                    LaunchedEffect(uiState.currentUser) {
                        redirectToLogin()
                    }
                } else {
                    HomeScreen(
                        userDisplayName = uiState.userDisplayName,
                        isLoading = uiState.isLoading,
                        onLogoutConfirmed = { homeViewModel.logout() },
                        weatherViewModel = weatherViewModel
                    )
                }
            }
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}