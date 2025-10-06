package com.example.weatherly.ui.profile

data class ProfileUiState(
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val streak: Int = 0,
    val badges: List<String> = emptyList(),
    val points: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)
