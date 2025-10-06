package com.example.weatherly

import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.weatherly.utils.Resource
import com.example.weatherly.viewmodel.WeatherViewModel
import java.util.*

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    onRetry: () -> Unit
) {
    // Collect the state from the ViewModel's StateFlow
    val state by viewModel.weatherState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val currentState = state) {
            is Resource.Loading -> {
                CircularProgressIndicator()
            }

            is Resource.Error -> {
                Text("Error: ${currentState.message}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }

            is Resource.Success -> {
                // Data is already unwrapped and of the correct type
                val w = currentState.data
                Text(text = w.city, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(8.dp))
                // Icon (Glide loaded into an ImageView inside Compose)
                val iconUrl = "https://openweathermap.org/img/wn/${w.icon}@4x.png"

                AndroidView(
                    factory = { ctx ->
                        ImageView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                        }
                    },
                    update = { imageView ->
                        Glide.with(imageView.context)
                            .load(iconUrl)
                            .into(imageView)
                    },
                    modifier = Modifier
                        .size(120.dp) // Use a fixed size for clarity
                )

                Spacer(Modifier.height(8.dp))
                Text(text = viewModel.formatTemperature(w.temperature), style = MaterialTheme.typography.headlineSmall)
                // Use recommended way to capitalize
                Text(text = w.description.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }, style = MaterialTheme.typography.bodyLarge)

                Spacer(Modifier.height(12.dp))
                Text(text = clothingSuggestion(w.temperature), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

fun clothingSuggestion(tempC: Double): String {
    return when {
        tempC < 5 -> "Very cold — heavy coat, hat, gloves."
        tempC in 5.0..12.0 -> "Cold — jacket + layers."
        tempC in 12.0..20.0 -> "Cool — light jacket or sweater."
        tempC in 20.0..27.0 -> "Comfortable — t-shirt or light shirt."
        tempC >= 27.0 -> "Hot — shorts and breathable fabrics."
        else -> "Dress comfortably."
    }
}