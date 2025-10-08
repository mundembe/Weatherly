package com.example.weatherly

import android.app.Application
import android.content.Context
import com.example.weatherly.utils.LocaleManager
import com.example.weatherly.utils.NotificationHelper

class WeatherlyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Create the notification channel when the app starts
        NotificationHelper.createNotificationChannel(this)
    }

    override fun attachBaseContext(base: Context) {
        // FIX: Corrected typo from "attachBaseactx" to "attachBaseContext"
        super.attachBaseContext(LocaleManager.applySavedLocale(base))
    }
}
