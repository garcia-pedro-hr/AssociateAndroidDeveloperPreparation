package com.phgarcia.aadp.fragments

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.phgarcia.aadp.MyApplication
import com.phgarcia.aadp.R
import com.phgarcia.aadp.receivers.MyNotificationBroadcastReceiver
import com.phgarcia.aadp.utils.writtenInFull

class NotificationFragment : Fragment() {

    companion object {
        const val KEY_TEXT_REPLY = "key_text_reply"

        const val NOTIFICATION_ID_BASIC = 1
        const val NOTIFICATION_ID_ACTION = 2
        const val NOTIFICATION_ID_BUTTON = 3
        const val NOTIFICATION_ID_REPLY = 4
        const val NOTIFICATION_ID_EXPANSIBLE_TEXT = 5

        const val REQUEST_CODE_ACTION = 1000
        const val REQUEST_CODE_BUTTON_1 = 1001
        const val REQUEST_CODE_BUTTON_2 = 1002
        const val REQUEST_CODE_BUTTON_3 = 1003
        const val REQUEST_CODE_REPLY = 1004
    }

    private var counter = 0

    private val notificationText: String
        get() = getString(R.string.notification_counter, ++counter)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? =
            inflater.inflate(R.layout.fragment_notification, container, false).apply {

                findViewById<Button>(R.id.bt_basic_notification)
                        .setOnClickListener { launchBasicNotification() }

                findViewById<Button>(R.id.bt_action_notification)
                        .setOnClickListener { launchActionNotification() }

                findViewById<Button>(R.id.bt_button_notification)
                        .setOnClickListener { launchButtonNotification() }

                findViewById<Button>(R.id.bt_reply_notification)
                        .setOnClickListener { launchReplyNotification() }

                findViewById<Button>(R.id.bt_reply_expansible_text)
                        .setOnClickListener { launchExpansibleTextNotification() }

            }

    private fun launchBasicNotification() = context?.let {
        val builder = NotificationCompat.Builder(it, MyApplication.NOTIFICATION_CHANNEL_ID).apply {
            priority = NotificationCompat.PRIORITY_DEFAULT

            setSmallIcon(R.drawable.ic_chat_bubble_black)
            setContentTitle(getString(R.string.notification_basic))
            setContentText(notificationText)
        }

        showNotification(builder, NOTIFICATION_ID_BASIC)
    }

    private fun launchActionNotification() = context?.let {
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(it, REQUEST_CODE_ACTION,
                Intent(it, MyNotificationBroadcastReceiver::class.java).apply {
                    action = MyNotificationBroadcastReceiver.ACTION_NOTIF_CLICK
                    putExtra(MyNotificationBroadcastReceiver.EXTRA_NOTIFICATION_ID, NOTIFICATION_ID_ACTION)
                    putExtra(MyNotificationBroadcastReceiver.EXTRA_REQUEST_CODE, REQUEST_CODE_ACTION)
                }, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(it, MyApplication.NOTIFICATION_CHANNEL_ID).apply {
            priority = NotificationCompat.PRIORITY_DEFAULT

            setSmallIcon(R.drawable.ic_chat_bubble_black)
            setContentTitle(getString(R.string.notification_action))
            setContentText(notificationText)
            // Set the intent that will fire when the user taps the notification
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }

        showNotification(builder, NOTIFICATION_ID_ACTION)
    }

    private fun launchButtonNotification() = context?.let {
        val pendingIntent1 = PendingIntent.getBroadcast(it, REQUEST_CODE_BUTTON_1,
                Intent(it, MyNotificationBroadcastReceiver::class.java).apply {
                    action = MyNotificationBroadcastReceiver.ACTION_NOTIF_CLICK
                    putExtra(MyNotificationBroadcastReceiver.EXTRA_NOTIFICATION_ID, NOTIFICATION_ID_BUTTON)
                    putExtra(MyNotificationBroadcastReceiver.EXTRA_REQUEST_CODE, REQUEST_CODE_BUTTON_1)
                }, PendingIntent.FLAG_UPDATE_CURRENT)

        val pendingIntent2 = PendingIntent.getBroadcast(it, REQUEST_CODE_BUTTON_2,
                Intent(it, MyNotificationBroadcastReceiver::class.java).apply {
                    action = MyNotificationBroadcastReceiver.ACTION_NOTIF_CLICK
                    putExtra(MyNotificationBroadcastReceiver.EXTRA_NOTIFICATION_ID, NOTIFICATION_ID_BUTTON)
                    putExtra(MyNotificationBroadcastReceiver.EXTRA_REQUEST_CODE, REQUEST_CODE_BUTTON_2)
                }, PendingIntent.FLAG_UPDATE_CURRENT)

        val pendingIntent3 = PendingIntent.getBroadcast(it, REQUEST_CODE_BUTTON_3,
                Intent(it, MyNotificationBroadcastReceiver::class.java).apply {
                    action = MyNotificationBroadcastReceiver.ACTION_NOTIF_CLICK
                    putExtra(MyNotificationBroadcastReceiver.EXTRA_NOTIFICATION_ID, NOTIFICATION_ID_BUTTON)
                    putExtra(MyNotificationBroadcastReceiver.EXTRA_REQUEST_CODE, REQUEST_CODE_BUTTON_3)
                }, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(it, MyApplication.NOTIFICATION_CHANNEL_ID).apply {
            priority = NotificationCompat.PRIORITY_DEFAULT

            setSmallIcon(R.drawable.ic_chat_bubble_black)
            setContentTitle(getString(R.string.notification_button))
            setContentText(notificationText)

            // Pending Intents must have different request codes, otherwise they are reused
            addAction(R.drawable.ic_chat_bubble_black, 1.writtenInFull(it), pendingIntent1)
            addAction(R.drawable.ic_chat_bubble_black, 2.writtenInFull(it), pendingIntent2)
            addAction(R.drawable.ic_chat_bubble_black, 3.writtenInFull(it), pendingIntent3)
        }

        showNotification(builder, NOTIFICATION_ID_BUTTON)
    }

    private fun launchReplyNotification() = context?.let {
        val replyLabel: String = resources.getString(R.string.label_reply)
        val remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(replyLabel)
            build()
        }

        val replyPendingIntent: PendingIntent = PendingIntent.getBroadcast(it, REQUEST_CODE_REPLY,
                Intent(it, MyNotificationBroadcastReceiver::class.java).apply {
                    action = MyNotificationBroadcastReceiver.ACTION_NOTIF_REPLY
                    putExtra(MyNotificationBroadcastReceiver.EXTRA_NOTIFICATION_ID, NOTIFICATION_ID_REPLY)
                }, PendingIntent.FLAG_UPDATE_CURRENT)

        val action: NotificationCompat.Action =
                NotificationCompat.Action.Builder(R.drawable.ic_reply,
                        getString(R.string.label_reply),
                        replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build()

        val builder = NotificationCompat.Builder(it, MyApplication.NOTIFICATION_CHANNEL_ID).apply {
            priority = NotificationCompat.PRIORITY_DEFAULT
            color = it.getColor(R.color.black)

            setSmallIcon(R.drawable.ic_chat_bubble_black)
            setContentTitle(getString(R.string.notification_reply))
            setContentText(notificationText)
            addAction(action)
        }

        showNotification(builder, NOTIFICATION_ID_REPLY)
    }

    private fun launchExpansibleTextNotification() = context?.let {
        val builder = NotificationCompat.Builder(it, MyApplication.NOTIFICATION_CHANNEL_ID).apply {
            priority = NotificationCompat.PRIORITY_DEFAULT

            setSmallIcon(R.drawable.ic_chat_bubble_black)
            setContentTitle(getString(R.string.notification_expansible_text))
            setContentText(getString(R.string.lorem_ipsum_title))
            setStyle(NotificationCompat.BigTextStyle()
                    .bigText(getString(R.string.lorem_ipsum_text)))
        }

        showNotification(builder, NOTIFICATION_ID_EXPANSIBLE_TEXT)
    }

    private fun showNotification(builder: NotificationCompat.Builder, notificationId: Int) =
            context?.let {
                with(NotificationManagerCompat.from(it)) {
                    notify(notificationId, builder.build())
                }
            }
}