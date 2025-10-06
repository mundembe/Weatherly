package com.example.weatherly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier // <-- Add this import
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherly.ui.home.HomeScreenWrapper
import com.example.weatherly.ui.profile.ProfileScreen
import com.example.weatherly.ui.settings.SettingsScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier // <-- FIX: Add the modifier parameter
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier // <-- FIX: Apply the modifier to the NavHost
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreenWrapper()
        }
        composable(BottomNavItem.Profile.route) {
            ProfileScreen()
        }
        composable(BottomNavItem.Settings.route) {
            SettingsScreen()
        }
    }
}
