package com.example.weatherly.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    var uiState = androidx.compose.runtime.mutableStateOf(ProfileUiState())
        private set

    init {
        loadProfile()
    }

    fun loadProfile() {

        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoading = true)

            val user = auth.currentUser
            if (user == null) {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    error = "No user logged in"
                )
                return@launch
            }

            try {
                val profileDoc = firestore.collection("users").document(user.uid).get().await()

                val streak = profileDoc.getLong("streak")?.toInt() ?: 0
                val points = profileDoc.getLong("points")?.toInt() ?: 0
                val badges = profileDoc.get("badges") as? List<String> ?: emptyList()

                uiState.value = ProfileUiState(
                    displayName = user.displayName ?: "Anonymous",
                    email = user.email ?: "",
                    photoUrl = user.photoUrl?.toString(),
                    streak = streak,
                    points = points,
                    badges = badges,
                    isLoading = false
                )
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Failed to load profile"
                )
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        FirebaseAuth.getInstance().signOut()
        onLogout()
    }
}
