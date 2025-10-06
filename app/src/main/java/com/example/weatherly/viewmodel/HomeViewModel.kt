package com.example.weatherly.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherly.data.repository.GamificationRepository
import com.example.weatherly.ui.home.HomeUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update // If you use .update { ... }
import kotlinx.coroutines.launch

//// Define a UI state data class
//data class HomeUiState(
//    val currentUser: FirebaseUser? = null,
//    val isLoading: Boolean = false, // Example: for fetching additional profile data
//    val userDisplayName: String = "User" // Example: a processed display name
//)

class HomeViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    // Private MutableStateFlow that can be updated within the ViewModel
    private val _uiState = MutableStateFlow(HomeUiState())
    // Public StateFlow that is read-only from the outside
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val gamificationRepo = GamificationRepository()

//    var uiState = androidx.compose.runtime.mutableStateOf(HomeUiState())
//        private set

    init {
        // Initialize the state with the current user
        // and potentially process user data
        auth.currentUser?.let { user ->
            _uiState.update { currentState ->
                currentState.copy(
                    currentUser = user,
                    userDisplayName = user.displayName ?: user.email ?: "User"
                )
            }
            // If you need to fetch more data related to the user:
            // fetchUserSpecificData(user.uid)
        }

        // You could also set up an AuthStateListener here if you want
        // the ViewModel to reactively update to auth changes throughout the app's lifecycle,
        // though for a simple Home screen, checking on init might be enough.
    }

    // Example function to fetch additional user data
    // private fun fetchUserSpecificData(userId: String) {
    //     viewModelScope.launch {
    //         _uiState.update { it.copy(isLoading = true) }
    //         // Simulate network call or database query
    //         // val profile = repository.getUserProfile(userId)
    //         _uiState.update {
    //             it.copy(
    //                 // userProfile = profile, // Assuming you add userProfile to HomeUiState
    //                 isLoading = false
    //             )
    //         }
    //     }
    // }

    @RequiresApi(Build.VERSION_CODES.O)
    fun performDailyCheckIn() {
        viewModelScope.launch {
            // FIX: Use the thread-safe '.update' function on the private '_uiState'.
            _uiState.update { it.copy(isCheckingIn = true) }

            when (val result = gamificationRepo.checkIn()) {
                is GamificationRepository.CheckInResult.Success -> {
                    // FIX: Use '.update' to modify the state.
                    _uiState.update {
                        it.copy(
                            isCheckingIn = false,
                            message = "✅ Checked in! Streak: ${result.streak}",
                            streak = result.streak,
                            points = result.points,
                            badges = result.badges
                        )
                    }
                }
                is GamificationRepository.CheckInResult.AlreadyCheckedIn -> {
                    // FIX: Use '.update' to modify the state.
                    _uiState.update {
                        it.copy(
                            isCheckingIn = false,
                            message = "You’ve already checked in today!"
                        )
                    }
                }
                is GamificationRepository.CheckInResult.Error -> {
                    // FIX: Use '.update' to modify the state.
                    _uiState.update {
                        it.copy(
                            isCheckingIn = false,
                            message = "❌ ${result.message}"
                        )
                    }
                }
            }
        }
    }

    fun logout() {
        // Any pre-logout logic (e.g., clearing local caches managed by this ViewModel)
        // ...

        auth.signOut()

        // After sign-out, the currentUser in Firebase.auth will be null.
        // The Activity will observe the change in _uiState (if you update it here,
        // or re-check auth.currentUser on next recomposition) and navigate.
        // Or, the Activity can simply call this and then navigate.
        // For immediate navigation, the Activity handling it after calling logout() is fine.
        // To make the ViewModel trigger navigation more explicitly, you could add an event Flow/Channel.
        _uiState.update { it.copy(currentUser = null, userDisplayName = "User") }
    }
}
