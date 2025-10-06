package com.example.weatherly.ui.home

data class HomeUiState(
    val currentUser: Any? = null,
    val userDisplayName: String = "",
    val isLoading: Boolean = false,
    val isCheckingIn: Boolean = false,
    val message: String? = null,
    val streak: Int = 0,
    val points: Int = 0,
    val badges: List<String> = emptyList()
)
