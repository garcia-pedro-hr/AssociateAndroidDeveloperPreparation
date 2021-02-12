package com.phgarcia.aadp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.phgarcia.aadp.R
import com.phgarcia.aadp.utils.NotificationUtils

class NotificationFragment : Fragment() {
    private var counter = 0

    private val notificationText: String
        get() = getString(R.string.notification_counter, ++counter)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_notification, container, false).apply {

            findViewById<Button>(R.id.bt_basic_notification).setOnClickListener {
                NotificationUtils.launchTestBasicNotification(context, notificationText)
            }

            findViewById<Button>(R.id.bt_action_notification).setOnClickListener {
                NotificationUtils.launchTestActionNotification(context, notificationText)
            }

            findViewById<Button>(R.id.bt_button_notification).setOnClickListener {
                NotificationUtils.launchTestButtonNotification(context, notificationText)
            }

            findViewById<Button>(R.id.bt_reply_notification).setOnClickListener {
                NotificationUtils.launchTestReplyNotification(context, notificationText)
            }

            findViewById<Button>(R.id.bt_reply_expansible_text).setOnClickListener {
                NotificationUtils.launchTestExpansibleTextNotification(context)
            }
        }
}