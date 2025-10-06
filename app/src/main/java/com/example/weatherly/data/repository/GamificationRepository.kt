package com.example.weatherly.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDate.now

class GamificationRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun checkIn(): CheckInResult {
        val user = auth.currentUser ?: return CheckInResult.Error("User not logged in")
        val userDoc = firestore.collection("users").document(user.uid)

        val today = now().toString()

        try {
            val snapshot = userDoc.get().await()
            val lastCheckIn = snapshot.getString("lastCheckIn")
            var streak = snapshot.getLong("streak")?.toInt() ?: 0
            var points = snapshot.getLong("points")?.toInt() ?: 0
            val badges = snapshot.get("badges") as? MutableList<String> ?: mutableListOf()

            if (lastCheckIn != today) {
                // Determine if streak should continue
                if (lastCheckIn == now().minusDays(1).toString()) {
                    streak++
                } else {
                    streak = 1 // reset streak
                }

                // Add points and potential badges
                points += 10 // example: 10 points per check-in

                if (streak == 5 && "5-Day Streak" !in badges) badges.add("5-Day Streak")
                if (streak == 10 && "10-Day Legend" !in badges) badges.add("10-Day Legend")

                // Update Firestore
                userDoc.set(
                    mapOf(
                        "lastCheckIn" to today,
                        "streak" to streak,
                        "points" to points,
                        "badges" to badges
                    )
                ).await()

                return CheckInResult.Success(streak, points, badges)
            } else {
                return CheckInResult.AlreadyCheckedIn
            }
        } catch (e: Exception) {
            return CheckInResult.Error(e.localizedMessage ?: "Check-in failed")
        }
    }

    sealed class CheckInResult {
        data class Success(val streak: Int, val points: Int, val badges: List<String>) : CheckInResult()
        data object AlreadyCheckedIn : CheckInResult()
        data class Error(val message: String) : CheckInResult()
    }
}
