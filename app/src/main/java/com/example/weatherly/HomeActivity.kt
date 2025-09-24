package com.example.weatherly

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge // If you are using edge-to-edge
import androidx.appcompat.app.AlertDialog // For the confirmation dialog
import androidx.appcompat.app.AppCompatActivity
// Make sure you have ViewBinding enabled in your build.gradle (Module :app)
// buildFeatures { viewBinding = true }
import com.example.weatherly.databinding.ActivityHomeBinding // Import ViewBinding class
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth // Firebase Kotlin extensions
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // If you are using edge-to-edge

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Set up the logout button click listener
        binding.logoutBtn.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // You can update tvHelloWorld with user-specific info if needed
        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.tvHelloWorld.text = "Welcome, ${currentUser.email ?: "User"}!"
        } else {
            // This case should ideally not happen if HomeActivity is protected.
            // If it does, redirect to LoginActivity.
            redirectToLogin()
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Logout") { dialog, which ->
                logoutUser()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logoutUser() {
        auth.signOut()

        // Redirect to LoginActivity and clear back stack
        redirectToLogin()
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        // Clears the activity stack, so the user can't go back to HomeActivity after logging out.
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Finish HomeActivity
    }

    // Optional: If you want to handle the Android back press to prevent going back to a logged-out state
    // if something went wrong with activity stack clearing (though flags should handle it).
    // override fun onBackPressed() {
    //     super.onBackPressed() // Or handle it differently if needed, e.g., show exit confirmation
    //     // For simple logout, the flags on intent are usually sufficient.
    // }
}
