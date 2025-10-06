// File: app/src/main/java/com/example/weatherly/ui/home/HomeActivity.kt
package com.example.weatherly.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource // <-- ADD THIS IMPORT
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherly.LoginActivity
import com.example.weatherly.ui.navigation.BottomNavItem
import com.example.weatherly.ui.navigation.NavigationGraph
import com.example.weatherly.ui.theme.WeatherlyTheme
import com.example.weatherly.viewmodel.HomeViewModel
import com.example.weatherly.viewmodel.WeatherViewModel
import com.example.weatherly.viewmodel.WeatherViewModelFactory

class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(application)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherlyTheme {
                // Check for logged-in user and redirect if necessary
                val uiState by homeViewModel.uiState.collectAsState()
                if (uiState.currentUser == null) {
                    LaunchedEffect(uiState.currentUser) {
                        redirectToLogin()
                    }
                    return@WeatherlyTheme // Stop rendering if not logged in
                }

                val navController = rememberNavController()
                val items = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Profile,
                    BottomNavItem.Settings
                )

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route

                            items.forEach { item ->
                                NavigationBarItem(
                                    selected = currentRoute == item.route,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        // FIX: Use painterResource to load the drawable icon
                                        // specified in your BottomNavItem class.
                                        Icon(
                                            painter = painterResource(id = item.icon),
                                            contentDescription = item.label
                                        )
                                    },
                                    label = { Text(item.label) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    // Your NavigationGraph is correctly placed here
                    NavigationGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
