package com.example.weatherly.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape // <-- Add this import
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherly.data.api.ForecastResponse
import com.example.weatherly.viewmodel.ForecastType
import com.example.weatherly.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ForecastCard(
    forecast: ForecastResponse?,
    viewModel: WeatherViewModel
) {

    if (forecast == null) {
        Text("Forecast data not available offline.", modifier = Modifier.padding(16.dp))
        return
    }

    val forecastType by viewModel.forecastType

    Column(modifier = Modifier.padding(16.dp)) {
        ForecastTypeToggle(
            selectedType = forecastType,
            onTypeChange = { viewModel.setForecastType(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            if (forecastType == ForecastType.HOURLY) {
                items(forecast.hourly.take(24)) { hourly ->
                    ForecastItem(
                        time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(hourly.timestamp * 1000)),
                        temp = viewModel.formatTemperature(hourly.temp),
                        iconUrl = "https://openweathermap.org/img/wn/${hourly.weather.firstOrNull()?.icon}@2x.png"
                    )
                }
            } else {
                items(forecast.daily.take(7)) { daily ->
                    ForecastItem(
                        time = SimpleDateFormat("EEE", Locale.getDefault()).format(Date(daily.timestamp * 1000)),
                        temp = "H:${viewModel.formatTemperature(daily.temp.max)}\nL:${viewModel.formatTemperature(daily.temp.min)}",
                        iconUrl = "https://openweathermap.org/img/wn/${daily.weather.firstOrNull()?.icon}@2x.png"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForecastTypeToggle(
    selectedType: ForecastType,
    onTypeChange: (ForecastType) -> Unit
) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        SegmentedButton(
            selected = selectedType == ForecastType.HOURLY,
            onClick = { onTypeChange(ForecastType.HOURLY) },
            shape = RoundedCornerShape(topStartPercent = 50, bottomStartPercent = 50)
        ) {
            Text("Hourly")
        }
        SegmentedButton(
            selected = selectedType == ForecastType.DAILY,
            onClick = { onTypeChange(ForecastType.DAILY) },
            shape = RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50)
        ) {
            Text("Daily")
        }
    }
}
