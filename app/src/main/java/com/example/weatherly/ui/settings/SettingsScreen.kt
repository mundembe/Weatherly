package com.example.weatherly.ui.settings

import android.Manifest
import android.app.Activity
import android.app.Application
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
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
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    val settings by viewModel.userSettings.collectAsState()
    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) viewModel.updateNotifications(true)
        }
    )

    // Screen fade-in animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(150)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { 40 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { 40 })
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.headlineMedium
            )

            // 🌍 Language Section
            SettingCard(
                icon = Icons.Default.LocationOn,
                title = stringResource(R.string.language),
                content = {
                    DropdownSetting(
                        current = settings.language,
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
                }
            )

            // 🌡 Temperature Unit
            SettingCard(
                icon = Icons.Default.Star,
                title = stringResource(R.string.temperature_unit),
                content = {
                    DropdownSetting(
                        current = settings.temperatureUnit,
                        options = listOf(
                            "Celsius" to stringResource(R.string.celsius),
                            "Fahrenheit" to stringResource(R.string.fahrenheit)
                        )
                    ) { unit -> viewModel.updateTemperatureUnit(unit) }
                }
            )

            // 🔔 Notifications
            SettingCard(
                icon = Icons.Default.Notifications,
                title = stringResource(R.string.notifications),
                content = {
                    Switch(
                        checked = settings.notificationsEnabled,
                        onCheckedChange = { isEnabled ->
                            if (isEnabled) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                else
                                    viewModel.updateNotifications(true)
                            } else viewModel.updateNotifications(false)
                        }
                    )
                }
            )
        }
    }
}

/**
 * Individual setting section card with icon + title + animated content
 */
@Composable
fun SettingCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
                content()
            }
        }
    }
}


/**
 * Animated dropdown menu with Material styling
 */
@Composable
fun DropdownSetting(
    current: String,
    options: List<Pair<String, String>>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(options.firstOrNull { it.first == current }?.second ?: current)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
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
