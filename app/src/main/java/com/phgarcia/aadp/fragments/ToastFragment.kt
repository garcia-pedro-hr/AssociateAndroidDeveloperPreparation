package com.phgarcia.aadp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.phgarcia.aadp.R

class ToastFragment : Fragment() {

    private var counter = 0

    private val toastText: String
        get() = getString(R.string.toast_counter, ++counter)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_toast, container, false).apply {

            findViewById<Button>(R.id.bt_short_toast)
                .setOnClickListener { showShortToast() }

            findViewById<Button>(R.id.bt_long_toast)
                .setOnClickListener { showLongToast() }

            findViewById<Button>(R.id.bt_custom_toast)
                .setOnClickListener { showCustomToast() }

        }

    private fun showShortToast() =
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()

    private fun showLongToast() =
        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()

    private fun showCustomToast() {
        activity?.let {
            val inflater: LayoutInflater = layoutInflater
            val container: ViewGroup? = view?.findViewById(R.id.custom_toast_container)
            val layout: ViewGroup = inflater.inflate(R.layout.toast_custom, container) as ViewGroup
            val toastTextView: TextView = layout.findViewById(R.id.toast_text)

            toastTextView.text = toastText

            with (Toast(it.applicationContext)) {
                duration = Toast.LENGTH_LONG
                view = layout
                show()
            }
        }

    }
}