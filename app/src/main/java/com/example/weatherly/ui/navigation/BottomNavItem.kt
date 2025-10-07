package com.example.weatherly.ui.navigation


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.weatherly.R

sealed class BottomNavItem(
    val route: String,
    @StringRes val labelResId: Int, // <-- CHANGE to resource ID (Int)
    @DrawableRes val icon: Int
) {
    // FIX: Use R.string resource IDs instead of hardcoded strings
    object Home : BottomNavItem("home", R.string.home, R.drawable.ic_home)
    object Profile : BottomNavItem("profile", R.string.profile, R.drawable.ic_profile)
    object Settings : BottomNavItem("settings", R.string.settings, R.drawable.ic_settings)
}