package com.example.weatherly.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherly.WeatherScreen
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn // <-- IMPORT LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.weatherly.utils.Resource // <-- FIX: Import your own Resource class
import com.example.weatherly.viewmodel.HomeViewModel
import com.example.weatherly.viewmodel.WeatherViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userDisplayName: String,
    isLoading: Boolean,
    onLogoutConfirmed: () -> Unit,
    weatherViewModel: WeatherViewModel,
    homeViewModel: HomeViewModel,
    uiState: HomeUiState
) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(Unit) {
        weatherViewModel.fetchWeather()
        startAnimation = true
    }

    val weatherState by weatherViewModel.weatherState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // FIX: Use LazyColumn to make the content scrollable
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(alphaAnim), // Apply fade-in animation
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp), // Space between items
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                item {
                    Text(
                        text = "Welcome, $userDisplayName!",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                // --- Weather Card ---
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(20.dp),
                                spotColor = MaterialTheme.colorScheme.primary
                            ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        // REVISED: Pass the specific state to WeatherScreen
                        WeatherScreen(
                            weatherState = weatherState,
                            viewModel = weatherViewModel,
                            onRetry = { weatherViewModel.fetchWeather() }
                        )
                    }
                }

                // --- Forecast Card ---
                if (weatherState is Resource.Success) {
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            ForecastCard(
                                forecast = (weatherState as Resource.Success).data.forecast,
                                viewModel = weatherViewModel
                            )
                        }
                    }
                }

                // --- Gamification Card ---
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            uiState.message?.let {
                                Text(it, style = MaterialTheme.typography.bodyLarge)
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            Button(
                                onClick = { homeViewModel.performDailyCheckIn() },
                                modifier = Modifier.fillMaxWidth(0.8f),
                                enabled = !uiState.isCheckingIn,
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                            ) {
                                Text(if (uiState.isCheckingIn) "Checking In..." else "Daily Check-In")
                            }
                        }
                    }
                }
            }

            if (isLoading && !uiState.isCheckingIn) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
