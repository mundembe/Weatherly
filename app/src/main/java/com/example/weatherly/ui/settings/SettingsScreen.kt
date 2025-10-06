package com.example.weatherly.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherly.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = viewModel()) {
    val settings by viewModel.userSettings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        // Language selector
        Text("Language", style = MaterialTheme.typography.titleMedium)
        DropdownSetting(
            current = settings.language,
            options = listOf("en" to "English", "zu" to "isiZulu", "af" to "Afrikaans"),
            onSelect = { lang ->
                viewModel.updateLanguage(lang)
            }
        )

        Spacer(Modifier.height(24.dp))

        // Temperature unit selector
        Text("Temperature Unit", style = MaterialTheme.typography.titleMedium)
        DropdownSetting(
            current = settings.temperatureUnit,
            options = listOf("Celsius" to "Celsius", "Fahrenheit" to "Fahrenheit"),
            onSelect = { unit ->
                viewModel.updateTemperatureUnit(unit)
            }
        )

        Spacer(Modifier.height(24.dp))

        // Notifications toggle
        Text("Notifications", style = MaterialTheme.typography.titleMedium)
        Switch(
            checked = settings.notificationsEnabled,
            onCheckedChange = { viewModel.updateNotifications(it) }
        )
    }
}

@Composable
fun DropdownSetting(
    current: String,
    options: List<Pair<String, String>>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(options.firstOrNull { it.first == current }?.second ?: current)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { (code, name) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onSelect(code)
                        expanded = false
                    }
                )
            }
        }
    }
}
