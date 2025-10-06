package com.example.weatherly

import android.app.Application
import android.content.Context
import com.example.weatherly.utils.LocaleManager

class WeatherlyApp : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.applySavedLocale(base))
    }
}
