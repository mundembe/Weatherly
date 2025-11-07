package com.example.weatherly.ui.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.weatherly.data.model.WeatherData
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment

@Composable
fun CurrentWeatherCard(
    data: WeatherData,
    formatTemperature: (Double) -> String
) {
    val current = data.current
    val weather = current.weather.firstOrNull()

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://openweathermap.org/img/wn/${weather?.icon ?: "01d"}@4x.png",
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = current.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = formatTemperature(current.main.temp),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = weather?.description?.replaceFirstChar { it.uppercase() } ?: "—",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
