package com.example.weatherly.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherly.R
import com.example.weatherly.viewmodel.HomeViewModel

@Composable
// The signature remains correct, accepting the viewModel and the logout action
fun ProfileScreen(
    homeViewModel: HomeViewModel,
    onLogout: () -> Unit
) {
    // We still need the uiState to get the user's display name.
    val uiState by homeViewModel.uiState.collectAsState()

    // --- SIMPLIFIED LAYOUT ---
    // The Column now directly displays the user info and logout button
    // without the complex 'when' block for loading/error states.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // This will center the content vertically
    ) {
        // Display a welcome message
        Text(
            text = "Welcome,",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        // Display Name from the HomeViewModel's state
        Text(
            // We use a fallback "..." in case the name isn't loaded yet
            text = uiState.userDisplayName.ifEmpty { "..." },
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(32.dp))

        // The Logout Button remains the same
        Button(
            onClick = {
                // The onLogout lambda handles navigation, as defined in HomeActivity
                onLogout()
            },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(stringResource(R.string.logout))
        }
    }
}
