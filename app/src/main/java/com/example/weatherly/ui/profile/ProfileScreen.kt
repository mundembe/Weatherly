package com.example.weatherly.ui.profile

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.weatherly.LoginActivity
import com.example.weatherly.R

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel = viewModel()) {
    val state by profileViewModel.uiState
    val context = LocalContext.current

    // --- FIX: Use a single Column for the entire screen layout ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- Handle the different UI states inside the main layout ---
        when {
            // 1. Loading State
            state.isLoading -> {
                // Show a loading indicator but keep the rest of the layout structure
                Spacer(Modifier.weight(1f))
                CircularProgressIndicator()
                Spacer(Modifier.weight(1f))
            }
            // 2. Error State
            state.error != null -> {
                // Show error message and a retry button
                Spacer(Modifier.weight(1f))
                Text(
                    "Error: ${state.error}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(8.dp))
                Button(onClick = { profileViewModel.loadProfile() }) {
                    Text("Retry")
                }
                Spacer(Modifier.weight(1f))
            }
            // 3. Success State
            else -> {
                // User avatar
                val painter = if (state.photoUrl != null) {
                    rememberAsyncImagePainter(state.photoUrl)
                } else {
                    null
                }

                if (painter != null) {
                    Image(
                        painter = painter,
                        contentDescription = stringResource(R.string.profile),
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "No profile image",
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(Modifier.height(12.dp))
                Text(state.displayName, style = MaterialTheme.typography.headlineSmall)
                Text(
                    state.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(24.dp))

                // Streaks and Points
                Text("🔥 Streak: ${state.streak} days", style = MaterialTheme.typography.bodyLarge)
                Text("⭐ Points: ${state.points}", style = MaterialTheme.typography.bodyLarge)

                Spacer(Modifier.height(16.dp))

                // Badges
                if (state.badges.isNotEmpty()) {
                    Text("🏅 Badges:", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.badges.size) { index ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                            ) {
                                Text(
                                    text = state.badges[index],
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
                // This spacer pushes the logout button down only in the success state
                Spacer(Modifier.weight(1f))
            }
        }

        // --- The Logout Button is now OUTSIDE the when block ---
        // It will always be visible at the bottom, except during the initial full-screen load if you prefer that.
        // If an error occurs, it will be at the bottom of the screen.
        if(!state.isLoading) { // Optionally hide button during loading
            Spacer(Modifier.weight(1f)) // This pushes the button down if the content above is short (like the error message)
            Button(
                onClick = {
                    profileViewModel.logout {
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(stringResource(R.string.logout))
            }
        }
    }
}
