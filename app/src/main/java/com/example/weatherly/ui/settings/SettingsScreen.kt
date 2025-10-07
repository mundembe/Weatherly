package com.example.weatherly.ui.settings

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherly.R
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

    Column(Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        // FIX: Use stringResource for the title
        Text(stringResource(R.string.settings), style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        // 🌍 Language Selector
        // FIX: Use stringResource for the section title
        Text(stringResource(R.string.language), style = MaterialTheme.typography.titleMedium)
        DropdownSetting(
            current = settings.language,
            // FIX: Use stringResource for labels
            options = listOf(
                "en" to stringResource(R.string.english),
                "zu" to stringResource(R.string.isizulu),
                "af" to stringResource(R.string.afrikaans)
            )
        ) { lang ->
            viewModel.updateLanguage(lang)
            LocaleManager.setLocale(context, lang)
            (context as? Activity)?.recreate()
        }

        Spacer(Modifier.height(16.dp))

        // 🌡 Temperature Unit Selector
        Text(stringResource(R.string.temperature_unit), style = MaterialTheme.typography.titleMedium)
        DropdownSetting(
            current = settings.temperatureUnit,
            options = listOf(
                "Celsius" to stringResource(R.string.celsius),
                "Fahrenheit" to stringResource(R.string.fahrenheit)
            )
        ) { unit ->
            viewModel.updateTemperatureUnit(unit)
        }

        Spacer(Modifier.height(16.dp))

        // 🔔 Notifications Toggle
        Row(verticalAlignment = Alignment.CenterVertically) {
            // FIX: Use stringResource for the label
            Text(stringResource(R.string.notifications), Modifier.weight(1f))
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

    Box {
        TextButton(onClick = { expanded = true }) {
            // Display the translated label corresponding to the current key
            Text(options.firstOrNull { it.first == current }?.second ?: current)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { (value, label) ->
                DropdownMenuItem(
                    text = { Text(label) }, // `label` is already translated
                    onClick = {
                        expanded = false
                        onSelect(value) // `value` is the key (e.g., "en", "zu")
                    }
                )
            }
        }
    }
}

