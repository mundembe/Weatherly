package com.example.weatherly.ui.home


import WeatherSection
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherly.BuildConfig
import com.example.weatherly.utils.Resource
import com.example.weatherly.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userDisplayName: String,
    isLoading: Boolean,
    onLogoutConfirmed: () -> Unit,
    weatherViewModel: WeatherViewModel
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var city by remember { mutableStateOf("Durban") }

    // Fetch weather on first load
    LaunchedEffect(Unit) {
        weatherViewModel.fetchWeather(city)
    }

    val weatherState by weatherViewModel.weatherState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Text(
                    text = "Welcome, $userDisplayName!",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Weather Section
                // FIX: Check the state and pass the correct data to WeatherSection
                when (val state = weatherState) {
                    is Resource.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                    is Resource.Success -> {
                        WeatherSection(
                            weather = state.data,
                            onRefresh = { weatherViewModel.fetchWeather(city) }
                        )
                    }
                    is Resource.Error -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error: ${state.message}", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { weatherViewModel.fetchWeather(city) }) {
                                Text("Retry")
                            }
                        }
                    }
                }


                Spacer(modifier = Modifier.height(32.dp))

                // Logout Button
                Button(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.fillMaxWidth(0.7f),
                    enabled = !isLoading
                ) {
                    Text("Logout")
                }
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        // Logout Confirmation Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Logout") },
                text = { Text("Are you sure you want to log out?") },
                confirmButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                        onLogoutConfirmed()
                    }) {
                        Text("Logout")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}