package com.example.weatherly.ui.home


import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.weatherly.R

/**
 * Selects a background drawable based on the OpenWeatherMap weather condition ID.
 *
 * @param weatherId The ID from the API response (e.g., 800 for clear, 501 for moderate rain).
 * @return A Painter resource for the corresponding background image.
 */
@Composable
fun getBackgroundForWeather(weatherId: Int): Int {
    return when (weatherId) {
        in 200..232 -> R.drawable.background_thunder // Thunderstorm
        in 300..321 -> R.drawable.background_drizzle // Drizzle
        in 500..531 -> R.drawable.background_rain   // Rain
        in 600..622 -> R.drawable.background_snow   // Snow
        in 701..781 -> R.drawable.background_atmosphere // Atmosphere (mist, fog, etc.)
        800 -> R.drawable.background_clear         // Clear
        in 801..804 -> R.drawable.background_clouds // Clouds
        else -> R.drawable.background_home         // Default
    }
}