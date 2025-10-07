package com.example.weatherly.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LocaleManager {

    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)

        // Use the modern API for Android Tiramisu (33) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            config.setLocales(android.os.LocaleList(locale))
        else
        // Use the deprecated API for older versions
            @Suppress("DEPRECATION")
            config.setLocale(locale)

        // Persist the chosen language code so it can be loaded on next app start
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("language", languageCode).apply()

        // Return a new context with the updated configuration
        return context.createConfigurationContext(config)
    }

    // This function will be called from WeatherlyApp to load the saved language
    fun applySavedLocale(context: Context): Context {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val lang = prefs.getString("language", "en") ?: "en" // Default to English
        return setLocale(context, lang)
    }
}
