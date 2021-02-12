package com.phgarcia.aadp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.phgarcia.aadp.R

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
         inflater.inflate(R.layout.fragment_main, container, false).apply {

             findViewById<Button>(R.id.tv_toast).setOnClickListener {
                 findNavController().navigate(R.id.action_mainFragment_to_toastFragment)
             }

             findViewById<Button>(R.id.tv_snackbar).setOnClickListener {
                 findNavController().navigate(R.id.action_mainFragment_to_snackbarFragment)
             }

             findViewById<Button>(R.id.tv_notification).setOnClickListener {
                 findNavController().navigate(R.id.action_mainFragment_to_notificationFragment)
             }

             findViewById<Button>(R.id.tv_work_manager).setOnClickListener {
                 findNavController().navigate(R.id.action_mainFragment_to_workManagerFragment)
             }
         }

}