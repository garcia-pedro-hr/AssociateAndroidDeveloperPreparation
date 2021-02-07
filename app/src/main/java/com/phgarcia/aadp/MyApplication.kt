package com.phgarcia.aadp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class MyApplication: Application() {

    companion object {
        const val NOTIFICATION_CHANNEL_NAME: String = "NOTIFICATION_CHANNEL_NAME"
        const val NOTIFICATION_CHANNEL_DESCRIPTION: String = "NOTIFICATION_CHANNEL_DESCRIPTION"
        const val NOTIFICATION_CHANNEL_ID: String = "NOTIFICATION_CHANNEL_ID"
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel  = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = NOTIFICATION_CHANNEL_DESCRIPTION
            }

            // Register the channel with the system
            with(getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager) {
                createNotificationChannel(channel)
            }
        }
    }
}