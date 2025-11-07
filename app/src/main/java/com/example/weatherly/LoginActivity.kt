package com.example.weatherly

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherly.ui.home.HomeActivity
import com.example.weatherly.ui.theme.WeatherlyTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // You need to get this web client ID from your google-services.json file
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent { // Use setContent for Compose
            WeatherlyTheme { // Apply your app's Compose theme
                LoginScreen(
                    auth = auth,
                    googleSignInClient = googleSignInClient, // Pass the client

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
    googleSignInClient: GoogleSignInClient, // Get the client
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current // For Toasts or other context-dependent operations
    val coroutineScope = rememberCoroutineScope()

    // --- ADD: Launcher for the Google Sign-In activity ---
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    val idToken = account.idToken!!
                    // Now, authenticate with Firebase
                    isLoading = true
                    coroutineScope.launch {
                        try {
                            val credential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(credential).await()
                            onLoginSuccess()
                        } catch (e: Exception) {
                            errorMessage = e.localizedMessage ?: "Firebase authentication failed."
                        } finally {
                            isLoading = false
                        }
                    }
                } catch (e: ApiException) {
                    errorMessage = "Google Sign-In failed: ${e.localizedMessage}"
                }
            } else {
                errorMessage = "Google Sign-In was cancelled or failed."
            }
        }
    )

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

            // --- ADD: Google Sign-In Button and Divider ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text(" OR ", modifier = Modifier.padding(horizontal = 8.dp))
                Divider(modifier = Modifier.weight(1f))
            }

            Button(
                onClick = {
                    isLoading = true
                    errorMessage = null
                    val signInIntent = googleSignInClient.signInIntent
                    googleSignInLauncher.launch(signInIntent)
                },
                modifier = Modifier.fillMaxWidth(),
                // Use different colors for branding
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
//                Icon(
//                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_google_logo),
//                    contentDescription = "Google Logo",
//                    modifier = Modifier.size(24.dp),
//                    tint = Color.Unspecified // Important for brand logos
//                )
//                Spacer(modifier = Modifier.width(12.dp))
                Text("Sign in with Google")
            }
        }
    }
}


