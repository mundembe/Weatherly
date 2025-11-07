package com.example.weatherly.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
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
            // FIX: HomeScreen now only needs the WeatherViewModel
            // It manages its own state internally.
            HomeScreen(viewModel = weatherViewModel)
        }
        composable(BottomNavItem.Profile.route) {
            // The ProfileScreen is the correct place for user-specific info
            // and actions like logging out.
            ProfileScreen(
                homeViewModel = homeViewModel,
                onLogout = { homeViewModel.logout() }
            )
        }
        composable(BottomNavItem.Settings.route) {
            SettingsScreen()
        }
    }
}
