package com.example.weatherly.ui.home

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherly.WeatherViewModelFactory
import com.example.weatherly.data.model.WeatherData
import com.example.weatherly.ui.weather.ActivityRecommendationCard
import com.example.weatherly.ui.weather.ClothingRecommendationCard
import com.example.weatherly.ui.weather.CurrentWeatherCard
import com.example.weatherly.ui.weather.ForecastList
import com.example.weatherly.ui.weather.ForecastToggle
import com.example.weatherly.utils.Resource
import com.example.weatherly.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    val state by viewModel.weatherState.collectAsState()
    val forecastType by viewModel.forecastType
    val cityQuery by viewModel.cityQuery

    var searchQuery by remember { mutableStateOf(TextFieldValue(cityQuery)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.updateCityQuery(it.text)
            },
            label = { Text("Search city") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { viewModel.fetchWeather() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            Spacer(Modifier.width(4.dp))
            Text("Fetch")
        }

        Spacer(Modifier.height(16.dp))

        // Main Weather State Handler
        when (state) {
            is Resource.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is Resource.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = (state as Resource.Error).message ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is Resource.Success -> {
                val data = (state as Resource.Success<WeatherData>).data

                // 🌤 Current Weather Card
                CurrentWeatherCard(
                    data = data,
                    formatTemperature = { viewModel.formatTemperature(it) }
                )

                Spacer(Modifier.height(16.dp))

                // Toggle (Hourly / Daily)
                ForecastToggle(
                    selected = forecastType,
                    onSelect = { viewModel.setForecastType(it) }
                )

                Spacer(Modifier.height(12.dp))

                // Forecast List
                val forecastList = viewModel.getFilteredForecast(data.forecast.list)

                AnimatedVisibility(visible = forecastList.isNotEmpty()) {
                    ForecastList(
                        items = forecastList,
                        formatTemperature = { viewModel.formatTemperature(it) }
                    )
                }

                // RECOMMENDATION CARDS
                Spacer(Modifier.height(16.dp))
                ClothingRecommendationCard(currentWeather = data.current)
                Spacer(Modifier.height(8.dp))
                ActivityRecommendationCard(currentWeather = data.current)
            }
        }
    }
}
