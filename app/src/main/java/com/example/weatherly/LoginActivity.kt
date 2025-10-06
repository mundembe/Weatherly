package com.example.weatherly

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherly.ui.home.HomeActivity
import com.example.weatherly.ui.home.MainActivity
import com.example.weatherly.ui.theme.WeatherlyTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : ComponentActivity() { // Changed to ComponentActivity
    private lateinit var auth: FirebaseAuth
    // Removed: private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        setContent { // Use setContent for Compose
            WeatherlyTheme { // Apply your app's Compose theme
                LoginScreen(
                    auth = auth,
                    onLoginSuccess = {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    },
                    onNavigateToRegister = {
                        startActivity(Intent(this, RegisterActivity::class.java))
                        // Optional: finish() here if you don't want LoginActivity in backstack
                    }
                )
            }
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

@OptIn(ExperimentalMaterial3Api::class) // For Material 3 components like OutlinedTextField
@Composable
fun LoginScreen(
    auth: FirebaseAuth,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current // For Toasts or other context-dependent operations

    Surface(modifier = Modifier.fillMaxSize()) { // A surface container using the 'background' color from the theme
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 24.dp), // Similar padding to your XML
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it.trim() }, // Trim email input
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it }, // Passwords usually aren't trimmed
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            errorMessage = "Email and password cannot be empty."
                            // Toast.makeText(context, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        isLoading = true
                        errorMessage = null // Clear previous error

                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    onLoginSuccess()
                                } else {
                                    errorMessage = task.exception?.localizedMessage ?: "Authentication failed."
                                    // Toast.makeText(context, "Authentication failed: ${task.exception?.localizedMessage ?: "Unknown error"}", Toast.LENGTH_LONG).show()
                                }
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onNavigateToRegister,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Account")
            }

            // TODO You can add the Google Sign-In Button later if needed.
            // For now, let's keep it simple with email/password.
            // Spacer(modifier = Modifier.height(16.dp))
            // GoogleSignInButtonComposable() // You'd need to create this
        }
    }
}

// Basic Preview Function (Very useful for UI development)
@Preview(showBackground = true, name = "Login Screen Preview")
@Composable
fun LoginScreenPreview() {
    WeatherlyTheme {
        // Create a dummy FirebaseAuth for previewing, or pass null if your Composable handles it
        // For a non-interactive preview, you often don't need a real auth instance.
        LoginScreen(
            auth = Firebase.auth, // This will be a dummy for preview, won't actually sign in
            onLoginSuccess = { },
            onNavigateToRegister = { }
        )
    }
}
