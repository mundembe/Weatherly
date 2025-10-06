package com.example.weatherly.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherly.ui.home.HomeScreen
import com.example.weatherly.ui.profile.ProfileScreen
import com.example.weatherly.ui.settings.SettingsScreen
import com.example.weatherly.viewmodel.HomeViewModel
import com.example.weatherly.viewmodel.WeatherViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    // Pass the ViewModels as parameters
    homeViewModel: HomeViewModel,
    weatherViewModel: WeatherViewModel
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.route) {
            // Get the state from the HomeViewModel
            val uiState by homeViewModel.uiState.collectAsState()

            // Pass everything directly to HomeScreen
            HomeScreen(
                userDisplayName = uiState.userDisplayName,
                isLoading = uiState.isLoading,
                onLogoutConfirmed = { homeViewModel.logout() },
                weatherViewModel = weatherViewModel,
                homeViewModel = homeViewModel,
                uiState = uiState
            )
        }
        composable(BottomNavItem.Profile.route) {
            ProfileScreen()
        }
        composable(BottomNavItem.Settings.route) {
            SettingsScreen()
        }
    }
}
