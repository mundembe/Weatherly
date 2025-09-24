package com.example.weatherly

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherly.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding // Declare binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater) // Inflate the layout using view binding
        setContentView(binding.root) // Set the content view to the binding's root

        auth = Firebase.auth

        binding.registerBtn.setOnClickListener {
            val email = binding.emailField.text.toString().trim()
            val password = binding.passwordField.text.toString() // Passwords are typically not trimmed

            // --- Input Validation ---
            if (email.isEmpty()) {
                binding.emailField.error = "Email cannot be empty"
                binding.emailField.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailField.error = "Please enter a valid email address"
                binding.emailField.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.passwordField.error = "Password cannot be empty"
                binding.passwordField.requestFocus()
                return@setOnClickListener
            }

            // Firebase password requirements (e.g., at least 6 characters)
            // You might want to add more specific client-side checks if desired,
            // but Firebase will enforce its own rules.
            if (password.length < 6) {
                binding.passwordField.error = "Password must be at least 6 characters"
                binding.passwordField.requestFocus()
                return@setOnClickListener
            }

            // --- Show Progress and Disable Button ---
            binding.progressBar.visibility = View.VISIBLE // Assuming you have a ProgressBar with id "progressBar" in your layout
            binding.registerBtn.isEnabled = false

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    // --- Hide Progress and Enable Button ---
                    binding.progressBar.visibility = View.GONE
                    binding.registerBtn.isEnabled = true

                    if (task.isSuccessful) {
                        // Registration success
                        // You might want to send a verification email or automatically sign in the user
                        // For now, redirecting to HomeActivity
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finishAffinity() // Finish this activity and all parent activities in the task
                    } else {
                        // If registration fails, display a message to the user.
                        val errorMessage = task.exception?.localizedMessage ?: "Registration failed. Please try again."
                        Toast.makeText(
                            this,
                            "Registration failed: $errorMessage",
                            Toast.LENGTH_LONG // Use LENGTH_LONG for error messages
                        ).show()
                    }
                }
        }

        // Example: Add a click listener for a "Go to Login" button if you have one
        // binding.loginRedirectBtn.setOnClickListener {
        //     startActivity(Intent(this, LoginActivity::class.java))
        //     finish() // Optional: finish RegisterActivity
        // }
    }

    // Optional: Check if the user is already signed in (e.g., in onStart)
    // public override fun onStart() {
    //     super.onStart()
    //     val currentUser = auth.currentUser
    //     if (currentUser != null) {
    //         // User is already signed in, redirect to HomeActivity
    //         startActivity(Intent(this, HomeActivity::class.java))
    //         finishAffinity()
    //     }
    // }
}

