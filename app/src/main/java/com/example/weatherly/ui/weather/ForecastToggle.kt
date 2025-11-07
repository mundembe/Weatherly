package com.example.weatherly.ui.weather

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import com.example.weatherly.viewmodel.ForecastType
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp

@Composable
fun ForecastToggle(
    selected: ForecastType,
    onSelect: (ForecastType) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        FilterChip(
            selected = selected == ForecastType.HOURLY,
            onClick = { onSelect(ForecastType.HOURLY) },
            label = { Text("Hourly") },
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        FilterChip(
            selected = selected == ForecastType.DAILY,
            onClick = { onSelect(ForecastType.DAILY) },
            label = { Text("Daily") },
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
