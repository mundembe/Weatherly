package com.example.weatherly

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherly.ui.theme.WeatherlyTheme
import com.google.firebase.auth.FirebaseUser // Keep this for the HomeScreen Composable signature

class HomeActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherlyTheme {
                // Collect the UI state from the ViewModel
                val uiState by homeViewModel.uiState.collectAsState()

                // Check if the user is logged in based on the ViewModel's state
                if (uiState.currentUser == null) {
                    // Use LaunchedEffect for side effects like navigation that occur
                    // due to state changes and should only run once per state change (or Unit for once on composition)
                    LaunchedEffect(uiState.currentUser) { // Re-run if currentUser state changes to null
                        redirectToLogin()
                    }
                } else {
                    // Pass the necessary data from uiState to the HomeScreen
                    HomeScreen(
                        userDisplayName = uiState.userDisplayName, // Use processed name from ViewModel
                        isLoading = uiState.isLoading, // Pass loading state if needed by UI
                        onLogoutConfirmed = {
                            homeViewModel.logout()
                            // The LaunchedEffect above will handle redirection when uiState.currentUser becomes null
                            // Or, if logout() in ViewModel doesn't immediately clear currentUser state
                            // that the UI observes, you can call redirectToLogin() here as well.
                            // For this setup, LaunchedEffect is cleaner.
                        }
                    )
                }
            }
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        // Clear the activity stack to prevent users from going back to HomeActivity after logout
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Finish HomeActivity
    }
}

// --- HomeScreen and Dialog Composables ---
// You can keep these in the same file or move them to a dedicated UI file (e.g., home/HomeScreenComposables.kt)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userDisplayName: String,
    isLoading: Boolean, // Example: to show a global loading indicator
    onLogoutConfirmed: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .padding(16.dp), // Padding for the content column
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome!",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = userDisplayName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.fillMaxWidth(0.7f),
                    enabled = !isLoading // Disable button if something is loading globally
                ) {
                    Text("Logout")
                }
            }

            if (isLoading) {
                // Example of a global loading indicator for the screen
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        if (showLogoutDialog) {
            LogoutConfirmationDialog(
                onConfirmLogout = {
                    showLogoutDialog = false
                    onLogoutConfirmed()
                },
                onDismissDialog = {
                    showLogoutDialog = false
                }
            )
        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onConfirmLogout: () -> Unit,
    onDismissDialog: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissDialog,
        title = { Text("Logout") },
        text = { Text("Are you sure you want to log out?") },
        confirmButton = {
            TextButton(onClick = onConfirmLogout) {
                Text("Logout")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissDialog) {
                Text("Cancel")
            }
        }
    )
}

// --- Previews ---
@Preview(showBackground = true, name = "Home Screen Preview (Logged In)")
@Composable
fun HomeScreenLoggedInPreview() {
    WeatherlyTheme {
        HomeScreen(
            userDisplayName = "user@example.com",
            isLoading = false,
            onLogoutConfirmed = {}
        )
    }
}

@Preview(showBackground = true, name = "Home Screen Preview (Loading)")
@Composable
fun HomeScreenLoadingPreview() {
    WeatherlyTheme {
        HomeScreen(
            userDisplayName = "user@example.com",
            isLoading = true,
            onLogoutConfirmed = {}
        )
    }
}
