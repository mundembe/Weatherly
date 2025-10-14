package com.example.weatherly

import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.weatherly.data.repository.WeatherData
import com.example.weatherly.utils.Resource
import com.example.weatherly.utils.clothingSuggestion
import com.example.weatherly.viewmodel.WeatherViewModel
import java.util.*

@Composable
fun WeatherScreen(
    // FIX: Pass the entire state object to handle Loading/Error/Success here
    weatherState: Resource<WeatherData>,
    viewModel: WeatherViewModel,
    onRetry: () -> Unit
) {
    val cityQuery by viewModel.cityQuery
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = cityQuery,
            onValueChange = { viewModel.updateCityQuery(it) },
            label = { Text("Enter city name") },
            // ... (rest of TextField is unchanged)
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

        AnimatedContent(
            targetState = weatherState,
            transitionSpec = {
                scaleIn(animationSpec = tween(500)) togetherWith scaleOut(animationSpec = tween(500))
            },
            label = "WeatherStateAnimation"
        ) { targetState ->
            when (targetState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
                is Resource.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = targetState.message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRetry) {
                            Text("Retry")
                        }
                    }
                }
                is Resource.Success -> {
                    // FIX: Extract the currentWeather from the WeatherData object
                    val currentWeatherData = targetState.data.currentWeather
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = currentWeatherData.city, style = MaterialTheme.typography.headlineMedium)

                        val iconUrl = "https://openweathermap.org/img/wn/${currentWeatherData.icon}@4x.png"
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
                            text = viewModel.formatTemperature(currentWeatherData.temperature),
                            style = MaterialTheme.typography.displaySmall
                        )

                        Text(
                            text = currentWeatherData.description.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = clothingSuggestion(currentWeatherData.temperature),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
