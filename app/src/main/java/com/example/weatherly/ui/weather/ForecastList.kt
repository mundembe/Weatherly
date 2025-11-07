package com.example.weatherly.ui.weather

import androidx.compose.runtime.Composable
import com.example.weatherly.data.api.ForecastItem
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.lazy.LazyRow


@Composable
fun ForecastList(
    items: List<ForecastItem>,
    formatTemperature: (Double) -> String
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(items) { item ->
            ForecastCard(item, formatTemperature)
        }
    }
}

@Composable
fun ForecastCard(
    item: ForecastItem,
    formatTemperature: (Double) -> String
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(160.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            AsyncImage(
                model = "https://openweathermap.org/img/wn/${item.weather.firstOrNull()?.icon ?: "01d"}@2x.png",
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = formatTemperature(item.main.temp),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = item.dateTime.substring(5, 16),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
