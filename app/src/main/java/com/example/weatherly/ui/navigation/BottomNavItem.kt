package com.example.weatherly.ui.navigation


import androidx.annotation.DrawableRes
import com.example.weatherly.R

sealed class BottomNavItem(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int
) {
    object Home : BottomNavItem("home", "Home", R.drawable.ic_home)
    object Profile : BottomNavItem("profile", "Profile", R.drawable.ic_profile)
    object Settings : BottomNavItem("settings", "Settings", R.drawable.ic_settings)
}