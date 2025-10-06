package com.example.weatherly.ui.settings

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherly.utils.LocaleManager
import com.example.weatherly.viewmodel.SettingsViewModel
import com.example.weatherly.viewmodel.SettingsViewModelFactory

@Composable
fun SettingsScreen(
    // FIX: Provide the factory to the viewModel() function
    viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(LocalContext.current)
    )
) {
    val settings by viewModel.userSettings.collectAsState()
    val context = LocalContext.current

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        // 🌍 Language Selector
        Text("Language", style = MaterialTheme.typography.titleMedium)
        DropdownSetting(
            current = settings.language,
            options = listOf("en" to "English", "zu" to "isiZulu", "af" to "Afrikaans")
        ) { lang ->
            viewModel.updateLanguage(lang)
            LocaleManager.setLocale(context, lang)
            (context as? Activity)?.recreate()
        }

        Spacer(Modifier.height(16.dp))

        // 🌡 Temperature Unit Selector
        Text("Temperature Unit", style = MaterialTheme.typography.titleMedium)
        DropdownSetting(
            current = settings.temperatureUnit,
            options = listOf("Celsius" to "°C", "Fahrenheit" to "°F")
        ) { unit ->
            viewModel.updateTemperatureUnit(unit)
        }

        Spacer(Modifier.height(16.dp))

        // 🔔 Notifications Toggle
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Enable Notifications", Modifier.weight(1f))
            Switch(
                checked = settings.notificationsEnabled,
                onCheckedChange = { viewModel.updateNotifications(it) }
            )
        }
    }
}

@Composable
fun DropdownSetting(
    current: String,
    options: List<Pair<String, String>>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Box {
            TextButton(onClick = { expanded = !expanded }) {
                Text(options.firstOrNull { it.first == current }?.second ?: current)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { (value, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            expanded = false
                            onSelect(value)
                        }
                    )
                }
            }
        }
    }
}

