package com.example.weatherly

import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.weatherly.utils.Resource
import com.example.weatherly.utils.clothingSuggestion // <-- FIX: Import from utils
import com.example.weatherly.viewmodel.WeatherViewModel
import java.util.*

// FIX: Removed the duplicate clothingSuggestion function from this file.

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    onRetry: () -> Unit
) {
    val state by viewModel.weatherState.collectAsState()
    val cityQuery by viewModel.cityQuery
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = cityQuery,
            onValueChange = { viewModel.updateCityQuery(it) },
            label = { Text("Enter city name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.fetchWeather()
                    keyboardController?.hide()
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                viewModel.fetchWeather()
                keyboardController?.hide()
            })
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val currentState = state) {
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            is Resource.Error -> {
                Text(text = currentState.message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }

            is Resource.Success -> {
                val weatherData = currentState.data
                Text(text = weatherData.city, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(8.dp))

                val iconUrl = "https://openweathermap.org/img/wn/${weatherData.icon}@4x.png"
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
                        Glide.with(imageView.context).load(iconUrl).into(imageView)
                    },
                    modifier = Modifier.size(120.dp)
                )

                Text(
                    text = viewModel.formatTemperature(weatherData.temperature),
                    style = MaterialTheme.typography.displaySmall // Larger text for temp
                )

                Text(
                    text = weatherData.description.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = clothingSuggestion(weatherData.temperature),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
