package com.example.weatherly.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.weatherly.R

object NotificationHelper {

    private const val CHANNEL_ID = "weatherly_channel"
    private const val CHANNEL_NAME = "Weatherly Notifications"
    private const val NOTIFICATION_ID = 1

    /**
     * Creates the notification channel. This should be called once when the app starts.
     */
    fun createNotificationChannel(context: Context) {
        // Notification channels are only available on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for Weatherly app"
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Builds and displays a notification after checking for permission.
     */
    fun showTestNotification(context: Context) {
        // Use a correct, non-adaptive icon
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_cloud)
            .setContentTitle("Notifications Enabled")
            .setContentText("You will now receive notifications from Weatherly.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        // --- FIX: Add permission check before calling notify() ---
        with(NotificationManagerCompat.from(context)) {
            // For Android 13+, check for POST_NOTIFICATIONS permission.
            // For older versions, this check is not needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // If permission is not granted, do nothing.
                    // The request logic is in the UI layer (SettingsScreen).
                    return
                }
            }
            // If permission is granted (or not required on older APIs), show the notification.
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}
