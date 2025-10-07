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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherly.LoginActivity
import com.example.weatherly.ui.navigation.BottomNavItem
import com.example.weatherly.ui.navigation.NavigationGraph
import com.example.weatherly.ui.theme.WeatherlyTheme
import com.example.weatherly.viewmodel.HomeViewModel
import com.example.weatherly.viewmodel.WeatherViewModel
import com.example.weatherly.WeatherViewModelFactory

class HomeActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    // This correctly creates the WeatherViewModel with its factory
    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(application)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherlyTheme {
                val uiState by homeViewModel.uiState.collectAsState()
                if (uiState.currentUser == null) {
                    LaunchedEffect(uiState.currentUser) { redirectToLogin() }
                    return@WeatherlyTheme
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
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            // on the back stack as users select items
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            // Avoid multiple copies of the same destination when
                                            // reselecting the same item
                                            launchSingleTop = true
                                            // Restore state when reselecting a previously selected item
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = item.icon),
                                            contentDescription = stringResource(id = item.labelResId)
                                        )
                                    },
                                    label = { Text(stringResource(id = item.labelResId)) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    // FIX: Pass the view models created in the activity to the graph
                    NavigationGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        homeViewModel = homeViewModel,
                        weatherViewModel = weatherViewModel
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
