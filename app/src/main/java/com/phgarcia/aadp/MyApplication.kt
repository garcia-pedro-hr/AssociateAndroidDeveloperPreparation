package com.phgarcia.aadp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import timber.log.Timber
import timber.log.Timber.DebugTree


class MyApplication: Application() {

    companion object {
        const val NOTIFICATION_CHANNEL_NAME: String = "NOTIFICATION_CHANNEL_NAME"
        const val NOTIFICATION_CHANNEL_DESCRIPTION: String = "NOTIFICATION_CHANNEL_DESCRIPTION"
        const val NOTIFICATION_CHANNEL_ID: String = "NOTIFICATION_CHANNEL_ID"

        const val BITMAP_OUTPUT_PATH: String = "bitmap_filter_outputs"

        const val WORK_NAME_IMG_MANIPULATION: String = "image_manipulation"
        const val WORKER_DATA_KEY_IMAGE_URI: String = "worker_data_key_image_uri"
        const val WORKER_DATA_KEY_BLUR_LV: String = "worker_data_key_blur_lv"

        const val TAG_OUTPUT: String = "OUTPUT"
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(DebugTree())
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