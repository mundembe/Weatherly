package com.example.weatherly.utils

fun clothingSuggestion(temp: Double): String = when {
    temp < 10 -> "Bundle up! 🧥"
    temp < 20 -> "Light jacket recommended 🧶"
    temp < 30 -> "T-shirt weather 👕"
    else -> "Stay hydrated and cool 😎"
}