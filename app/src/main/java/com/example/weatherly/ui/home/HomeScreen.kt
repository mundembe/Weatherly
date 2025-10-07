package com.example.weatherly.ui.home

// TODO: REMOVE this import as it's no longer used
// import WeatherSection
import android.os.Build
import androidx.annotation.RequiresApi
// ADD this import for the correct screen
import com.example.weatherly.WeatherScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherly.utils.Resource
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
    var showLogoutDialog by remember { mutableStateOf(false) }
    // The default city is now managed by the ViewModel, so this is not needed here
    // var city by remember { mutableStateOf("Durban") }

    // Fetch weather on first load using the default city from the ViewModel
    LaunchedEffect(Unit) {
        weatherViewModel.fetchWeather()
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
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Text(
                    text = "Welcome, $userDisplayName!",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- FIX: Replace WeatherSection with WeatherScreen ---
                WeatherScreen(
                    viewModel = weatherViewModel,
                    onRetry = { weatherViewModel.fetchWeather() }
                )
                // --- End of Fix ---

                // Use Spacer to push content below to the bottom
                Spacer(modifier = Modifier.weight(1f))

                // Gamification Section
                uiState.message?.let {
                    Text(it, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = { homeViewModel.performDailyCheckIn() },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    enabled = !uiState.isCheckingIn
                ) {
                    Text(if (uiState.isCheckingIn) "Checking In..." else "Daily Check-In")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Logout Button
                Button(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    enabled = !isLoading
                ) {
                    Text("Logout")
                }
            }

            if (isLoading && !uiState.isCheckingIn) {
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
