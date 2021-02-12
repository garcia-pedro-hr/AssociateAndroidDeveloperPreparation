package com.phgarcia.aadp.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import com.phgarcia.aadp.R
import com.phgarcia.aadp.viewmodels.WorkManagerViewModel

class WorkManagerFragment : Fragment() {

    private val IMAGE_URI =
        Uri.parse("android.resource://com.phgarcia.aadp/drawable/dog_studying")

    private val viewModel: WorkManagerViewModel by viewModels()

    private lateinit var imagePreview: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var cancelButton: Button
    private lateinit var goButton: Button
    private lateinit var blurRadioGroup: RadioGroup

    private val blurLevel: Int
        get() = when (blurRadioGroup.checkedRadioButtonId) {
            R.id.rb_blur_lv_0 -> 0
            R.id.rb_blur_lv_1 -> 1
            R.id.rb_blur_lv_2 -> 2
            R.id.rb_blur_lv_3 -> 3
            else -> 1
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_work_manager, container, false).apply {
            imagePreview = findViewById<ImageView>(R.id.iv_preview).apply {
                setImageResource(R.drawable.dog_studying)
            }

            goButton = findViewById<Button>(R.id.bt_go).apply {
                setOnClickListener {
                    showWorkInProgress()
                    viewModel.applyBlur(IMAGE_URI, blurLevel)
                }
            }

            cancelButton = findViewById<Button>(R.id.bt_cancel).apply {
                viewModel.cancelWork()
            }

            progressBar = findViewById(R.id.progress_bar)
            blurRadioGroup = findViewById(R.id.rg_blur_lvs)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.outputUri.observe(viewLifecycleOwner) { uri ->
            imagePreview.setImageURI(uri)
            showWorkFinished()
        }
    }

    override fun onStop() {
        super.onStop()

        // If screen is stopping, cancel any pending work
        viewModel.cancelWork()
    }

    private fun showWorkFinished() {
        progressBar.visibility = View.GONE
        cancelButton.visibility = View.GONE
        goButton.visibility = View.VISIBLE
    }

    private fun showWorkInProgress() {
        progressBar.visibility = View.VISIBLE
        cancelButton.visibility = View.VISIBLE
        goButton.visibility = View.GONE
    }
}