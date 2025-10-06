package com.example.weatherly.ui.profile

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.weatherly.LoginActivity

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel = viewModel()) {
    val state by profileViewModel.uiState

    val context = LocalContext.current

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (state.error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // User avatar
        if (state.photoUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(state.photoUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
            )
        } else {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Person,
                contentDescription = "No profile image",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(Modifier.height(12.dp))
        Text(state.displayName, style = MaterialTheme.typography.headlineSmall)
        Text(state.email, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)

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

        Spacer(Modifier.height(24.dp))

        // Logout Button
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
            Text("Logout")
        }
    }
}
