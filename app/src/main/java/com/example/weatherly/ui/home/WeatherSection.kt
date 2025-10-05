
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherSection(
    weather: WeatherEntity?,
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
            Text("${weather.temperature}°C – ${weather.description}")
            Spacer(modifier = Modifier.height(8.dp))

            // Load weather icon
            GlideImage(
                model = "https://openweathermap.org/img/wn/${weather.icon}@2x.png",
                contentDescription = weather.description,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(clothingSuggestion(weather.temperature))
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRefresh) {
                Text("Refresh")
            }
        }
    }
}