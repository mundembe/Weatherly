package com.example.weatherly.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherly.data.api.WeatherResponse

// ------------------ Clothing Recommendation ------------------
@Composable
fun ClothingRecommendationCard(currentWeather: WeatherResponse) {
    val temp = currentWeather.main.temp
    val weatherDescription = currentWeather.weather.firstOrNull()?.description?.lowercase() ?: ""

    // Use emojis instead of icons
    val recommendationPair: Pair<String, String> = when {
        "rain" in weatherDescription || "drizzle" in weatherDescription ->
            "Umbrella & Raincoat" to "☔"
        temp < 5 ->
            "Heavy Jacket & Scarf" to "🧥🧣"
        temp < 15 ->
            "Warm Jacket" to "🧥"
        temp > 25 && "clear" in weatherDescription ->
            "T-Shirt & Sunglasses" to "👕🕶️"
        temp > 20 ->
            "Light T-Shirt" to "👕"
        else ->
            "Light Sweater" to "🧶"
    }

    val recommendation = recommendationPair.first
    val iconEmoji = recommendationPair.second

    RecommendationCard(
        title = "What to Wear",
        recommendation = recommendation,
        iconEmoji = iconEmoji
    )
}

// ------------------ Activity Recommendation ------------------
@Composable
fun ActivityRecommendationCard(currentWeather: WeatherResponse) {
    val temp = currentWeather.main.temp
    val weatherDescription = currentWeather.weather.firstOrNull()?.description?.lowercase() ?: ""

    val recommendationPair: Pair<String, String> = when {
        "rain" in weatherDescription || "storm" in weatherDescription ->
            "Movie Marathon" to "🎬"
        "snow" in weatherDescription || temp < 0 ->
            "Build a Snowman" to "☃️"
        temp > 28 && "clear" in weatherDescription ->
            "Go to the Beach" to "🏖️"
        temp > 20 && "clear" in weatherDescription ->
            "Go for a Picnic" to "🌸🥪"
        temp in 15.0..25.0 ->
            "Go for a Hike" to "🥾🌲"
        else ->
            "Visit a Museum" to "🏛️"
    }

    val recommendation = recommendationPair.first
    val iconEmoji = recommendationPair.second

    RecommendationCard(
        title = "Suggested Activities",
        recommendation = recommendation,
        iconEmoji = iconEmoji
    )
}

// ------------------ Generic Recommendation Card ------------------
@Composable
private fun RecommendationCard(
    title: String,
    recommendation: String,
    iconEmoji: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recommendation,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = iconEmoji,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            )
        }
    }
}
