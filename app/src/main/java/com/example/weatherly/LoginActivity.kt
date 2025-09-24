package com.example.weatherly

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherly.databinding.ActivityLoginBinding // Import generated binding class
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding // Declare binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater) // Inflate the layout
        setContentView(binding.root) // Set the content view to the binding's root

        auth = Firebase.auth

        binding.loginBtn.setOnClickListener {
            val email = binding.emailField.text.toString().trim() // Use trim()
            val password = binding.passwordField.text.toString() // Passwords usually aren't trimmed
            binding.emailField.error = "Invalid email"

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: Show a loading indicator here (e.g., ProgressBar)

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    // Hide loading indicator here
                    if (task.isSuccessful) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this, // 'this' is fine for Activity context
                            "Authentication failed: ${task.exception?.localizedMessage ?: "Unknown error"}", // Use localizedMessage and provide a fallback
                            Toast.LENGTH_LONG // Consider LENGTH_LONG for error messages
                        ).show()
                    }
                }
        }

        binding.registerRedirectBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is already signed in, redirect to HomeActivity
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
        