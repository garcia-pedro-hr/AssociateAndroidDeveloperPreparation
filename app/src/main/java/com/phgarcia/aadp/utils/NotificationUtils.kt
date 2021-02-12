package com.phgarcia.aadp.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.phgarcia.aadp.MyApplication
import com.phgarcia.aadp.R
import com.phgarcia.aadp.receivers.MyNotificationBroadcastReceiver

object NotificationUtils {

    const val KEY_TEXT_REPLY = "key_text_reply"

    private const val NOTIFICATION_ID_BASIC = 1
    private const val NOTIFICATION_ID_ACTION = 2
    private const val NOTIFICATION_ID_BUTTON = 3
    private const val NOTIFICATION_ID_REPLY = 4
    private const val NOTIFICATION_ID_EXPANSIBLE_TEXT = 5

    private const val REQUEST_CODE_ACTION = 1000
    private const val REQUEST_CODE_BUTTON_1 = 1001
    private const val REQUEST_CODE_BUTTON_2 = 1002
    private const val REQUEST_CODE_BUTTON_3 = 1003
    private const val REQUEST_CODE_REPLY = 1004

    /**
     * Launches a test basic notification with a fixed content title
     * @param context A Context
     * @param contentText A string to be set as the contentText of the notification
     */
    fun launchTestBasicNotification(context: Context?, contentText: String) = context?.let {
        val builder = NotificationCompat.Builder(it, MyApplication.NOTIFICATION_CHANNEL_ID).apply {
            priority = NotificationCompat.PRIORITY_DEFAULT

            setSmallIcon(R.drawable.ic_chat_bubble_black)
            setContentTitle(it.getString(R.string.notification_basic),)
            setContentText(contentText)
        }

        showNotification(it, builder, NOTIFICATION_ID_BASIC)
    }

    /**
     * Launches a test action notification with a fixed content title
     * On click, the notification triggers MyNotificationBroadcastReceiver
     * @param context A Context
     * @param contentText A string to be set as the contentText of the notification
     */
    fun launchTestActionNotification(context: Context?, contentText: String) = context?.let {
        val intent: Intent = Intent(it, MyNotificationBroadcastReceiver::class.java).apply {
            action = MyNotificationBroadcastReceiver.ACTION_NOTIF_CLICK

            putExtra(
                MyNotificationBroadcastReceiver.EXTRA_NOTIFICATION_ID,
                NOTIFICATION_ID_ACTION
            )

            putExtra(
                MyNotificationBroadcastReceiver.EXTRA_REQUEST_CODE,
                REQUEST_CODE_ACTION
            )
        }

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            it,
            REQUEST_CODE_ACTION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(it, MyApplication.NOTIFICATION_CHANNEL_ID).apply {
            priority = NotificationCompat.PRIORITY_DEFAULT

            setSmallIcon(R.drawable.ic_chat_bubble_black)
            setContentTitle(it.getString(R.string.notification_action))
            setContentText(contentText)
            // Set the intent that will fire when the user taps the notification
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }

        showNotification(it, builder, NOTIFICATION_ID_ACTION)
    }

    /**
     * Launches a test button notification with a fixed content title and three fixed actions
     * All buttons will trigger MyNotificationBroadcastReceiver with different parameters
     * @param context A Context
     * @param contentText A string to be set as the contentText of the notification
     */
    fun launchTestButtonNotification(context: Context?, contentText: String) = context?.let {
        val pendingIntent1 = PendingIntent.getBroadcast(
            it,
            REQUEST_CODE_BUTTON_1,
            Intent(it, MyNotificationBroadcastReceiver::class.java).apply {
                action = MyNotificationBroadcastReceiver.ACTION_NOTIF_CLICK

                putExtra(
                    MyNotificationBroadcastReceiver.EXTRA_NOTIFICATION_ID,
                    NOTIFICATION_ID_BUTTON
                )

                putExtra(
                    MyNotificationBroadcastReceiver.EXTRA_REQUEST_CODE,
                    REQUEST_CODE_BUTTON_1
                )
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val pendingIntent2 = PendingIntent.getBroadcast(
            it,
            REQUEST_CODE_BUTTON_2,
            Intent(it, MyNotificationBroadcastReceiver::class.java).apply {
                action = MyNotificationBroadcastReceiver.ACTION_NOTIF_CLICK

                putExtra(
                    MyNotificationBroadcastReceiver.EXTRA_NOTIFICATION_ID,
                    NOTIFICATION_ID_BUTTON
                )

                putExtra(
                    MyNotificationBroadcastReceiver.EXTRA_REQUEST_CODE,
                    REQUEST_CODE_BUTTON_2
                )
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val pendingIntent3 = PendingIntent.getBroadcast(
            it,
            REQUEST_CODE_BUTTON_3,
            Intent(it, MyNotificationBroadcastReceiver::class.java).apply {
                action = MyNotificationBroadcastReceiver.ACTION_NOTIF_CLICK

                putExtra(
                    MyNotificationBroadcastReceiver.EXTRA_NOTIFICATION_ID,
                    NOTIFICATION_ID_BUTTON
                )

                putExtra(
                    MyNotificationBroadcastReceiver.EXTRA_REQUEST_CODE,
                    REQUEST_CODE_BUTTON_3
                )
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(it, MyApplication.NOTIFICATION_CHANNEL_ID).apply {
            priority = NotificationCompat.PRIORITY_DEFAULT

            setSmallIcon(R.drawable.ic_chat_bubble_black)
            setContentTitle(it.getString(R.string.notification_button))
            setContentText(contentText)

            // Pending Intents must have different request codes, otherwise they are reused
            addAction(R.drawable.ic_chat_bubble_black, 1.writtenInFull(it), pendingIntent1)
            addAction(R.drawable.ic_chat_bubble_black, 2.writtenInFull(it), pendingIntent2)
            addAction(R.drawable.ic_chat_bubble_black, 3.writtenInFull(it), pendingIntent3)
        }

        showNotification(it, builder, NOTIFICATION_ID_BUTTON)
    }

    /**
     * Launches a test notification with a reply field and a fixed content title
     * Sending the reply will trigger MyNotificationBroadcastReceiver
     * @param context A Context
     * @param contentText A string to be set as the contentText of the notification
     */
    fun launchTestReplyNotification(context: Context?, contentText: String) = context?.let {
        val remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(it.getString(R.string.label_reply))
            build()
        }

        val replyIntent: Intent = Intent(it, MyNotificationBroadcastReceiver::class.java).apply {
            action = MyNotificationBroadcastReceiver.ACTION_NOTIF_REPLY
            putExtra(
                MyNotificationBroadcastReceiver.EXTRA_NOTIFICATION_ID,
                NOTIFICATION_ID_REPLY
            )
        }

        val replyPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            it,
            REQUEST_CODE_REPLY,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val action: NotificationCompat.Action =
            NotificationCompat.Action.Builder(R.drawable.ic_reply,
                it.getString(R.string.label_reply),
                replyPendingIntent)
            .addRemoteInput(remoteInput)
            .build()

        val builder = NotificationCompat.Builder(it, MyApplication.NOTIFICATION_CHANNEL_ID).apply {
            priority = NotificationCompat.PRIORITY_DEFAULT
            color = it.getColor(R.color.black)

            setSmallIcon(R.drawable.ic_chat_bubble_black)
            setContentTitle(it.getString(R.string.notification_reply))
            setContentText(contentText)
            addAction(action)
        }

        showNotification(it, builder, NOTIFICATION_ID_REPLY)
    }

    /**
     * Launches a test notification with a fixed expansible text and fixed content title
     * @param context A Context
     */
    fun launchTestExpansibleTextNotification(context: Context?) = context?.let {
        val builder = NotificationCompat.Builder(it, MyApplication.NOTIFICATION_CHANNEL_ID).apply {
            priority = NotificationCompat.PRIORITY_DEFAULT

            setSmallIcon(R.drawable.ic_chat_bubble_black)
            setContentTitle(it.getString(R.string.notification_expansible_text))
            setContentText(it.getString(R.string.lorem_ipsum_title))
            setStyle(
                NotificationCompat.BigTextStyle().bigText(it.getString(R.string.lorem_ipsum_text))
            )
        }

        showNotification(it, builder, NOTIFICATION_ID_EXPANSIBLE_TEXT)
    }

    /**
     * Shows a notification
     * @param context A Context
     * @param builder A Notification builder
     * @param notificationId The notification ID
     */
    private fun showNotification(
        context: Context?,
        builder: NotificationCompat.Builder,
        notificationId: Int
    ) = context?.let {
        with(NotificationManagerCompat.from(it)) {
            notify(notificationId, builder.build())
        }
    }
}