package com.phgarcia.aadp.receivers

import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.phgarcia.aadp.BuildConfig
import com.phgarcia.aadp.fragments.NotificationFragment.Companion.KEY_TEXT_REPLY

class MyNotificationBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_NOTIF_CLICK = "${BuildConfig.APPLICATION_ID}.NOTIFICATION_CLICK"
        const val ACTION_NOTIF_REPLY = "${BuildConfig.APPLICATION_ID}.NOTIFICATION_REPLY"

        const val EXTRA_NOTIFICATION_ID = "notification_id"
        const val EXTRA_REQUEST_CODE = "request_code"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            var toastMessage: String? = null
            val notificationId: Int? = intent?.extras?.getInt(EXTRA_NOTIFICATION_ID, -1)
            val requestCode: Int? = intent?.extras?.getInt(EXTRA_REQUEST_CODE, -1)

            when (intent?.action) {
                ACTION_NOTIF_CLICK -> {
                    toastMessage = "${intent.action}: $notificationId"
                    requestCode?.let { toastMessage += " -> $requestCode" }
                }

                ACTION_NOTIF_REPLY -> {
                    val remoteInput = RemoteInput.getResultsFromIntent(intent)
                    val reply = remoteInput.getCharSequence(KEY_TEXT_REPLY).toString()
                    toastMessage = "${intent.action}: $notificationId -> $reply"
                }
            }

            notificationId?.let {
                NotificationManagerCompat.from(context).cancel(notificationId)
            }

            Toast.makeText(it, toastMessage, Toast.LENGTH_LONG).show()
        }
    }
}