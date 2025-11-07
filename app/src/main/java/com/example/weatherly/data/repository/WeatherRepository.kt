package com.example.weatherly.data.repository

import android.util.Log
import com.example.weatherly.data.api.WeatherApi
import com.example.weatherly.data.db.WeatherDao
import com.example.weatherly.data.db.WeatherEntity
import com.example.weatherly.data.model.WeatherData
import retrofit2.HttpException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class WeatherRepository(private val api: WeatherApi, private val dao: WeatherDao) {

    // --- REVISED AND CORRECTED FUNCTION ---
    suspend fun getWeatherAndForecast(city: String, apiKey: String): WeatherData? {
        Log.d("WeatherRepo", "Attempting to fetch weather for $city")

        // 2. FETCH FROM API (Simplified Logic)
        Log.d("WeatherRepo", "Fetching fresh data from API for $city")
        return try {
            // Use coroutineScope and async to run both API calls concurrently
            coroutineScope {
                val currentResponseDeferred = async { api.getWeather(city, apiKey) }
                val forecastResponseDeferred = async { api.getForecast(city, apiKey) }

                val currentResponse = currentResponseDeferred.await()
                val forecastResponse = forecastResponseDeferred.await()

                Log.d("WeatherRepo", "Successfully fetched current: ${currentResponse.name}")
                Log.d("WeatherRepo", "Successfully fetched forecast for city: ${forecastResponse.city.name}")

                // 3. SAVE THE FRESH DATA TO THE CACHE
                dao.insertWeather(
                    WeatherEntity(
                        city = currentResponse.name,
                        temperature = currentResponse.main.temp,
                        description = currentResponse.weather.firstOrNull()?.description ?: "Unknown",
                        icon = currentResponse.weather.firstOrNull()?.icon ?: "01d"
                    )
                )
                Log.d("WeatherRepo", "Saved fresh data to cache for ${currentResponse.name}")

                // 4. RETURN THE COMBINED DATA
                WeatherData(current = currentResponse, forecast = forecastResponse)
            }
        } catch (e: HttpException) {
            // This is crucial for debugging 404s!
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("WeatherRepo", "HTTP Error: ${e.code()} - ${e.message()}. Body: $errorBody")
            // Throw a more descriptive exception for the ViewModel to catch
            throw Exception("Could not find weather for '$city'. Check city name.")
        } catch (e: Exception) {
            // --- FIX: Corrected syntax and improved logging ---
            Log.e("WeatherRepo", "Network or parsing error: ${e.message}", e)
            // Re-throw so the ViewModel knows something went wrong
            throw Exception("Could not connect to the server. Check internet connection.")
        }
    }
}
