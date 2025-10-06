
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherly.data.db.WeatherEntity
import com.example.weatherly.utils.clothingSuggestion
import com.example.weatherly.viewmodel.WeatherViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherSection(
    weather: WeatherEntity?,
    viewModel: WeatherViewModel,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (weather == null) {
            Text("Fetching weather...")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRefresh) {
                Text("Retry")
            }
        } else {
            Text(weather.city, style = MaterialTheme.typography.titleLarge)
            // FIX: Use the ViewModel to format the temperature
            Text(
                text = "${viewModel.formatTemperature(weather.temperature)} – ${weather.description}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            GlideImage(
                model = "https://openweathermap.org/img/wn/${weather.icon}@2x.png",
                contentDescription = weather.description,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(clothingSuggestion(weather.temperature)) // This suggestion is still based on Celsius
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRefresh) {
                Text("Refresh")
            }
        }
    }
}