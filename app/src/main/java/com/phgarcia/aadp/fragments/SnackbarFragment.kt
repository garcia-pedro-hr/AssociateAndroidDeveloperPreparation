package com.phgarcia.aadp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.phgarcia.aadp.R

class SnackbarFragment : Fragment() {

    companion object {
        private val TAG: String = SnackbarFragment::class.java.simpleName
    }

    private var counter = 0

    private val snackbarText: String
        get() = getString(R.string.snackbar_counter, ++counter)

    private var selectedAnimationMode: Int = Snackbar.ANIMATION_MODE_FADE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_snackbar, container, false).apply {

            findViewById<Button>(R.id.bt_short_snackbar)
                .setOnClickListener { showShortSnackbar() }

            findViewById<Button>(R.id.bt_long_snackbar)
                .setOnClickListener { showLongSnackbar() }

            findViewById<Button>(R.id.bt_indefinite_snackbar)
                .setOnClickListener { showIndefiniteSnackbar() }

            findViewById<RadioGroup>(R.id.rg_animation_modes).apply {

                setOnCheckedChangeListener { _, checkedId ->
                    onAnimationModeChanged(checkedId)
                    Log.d(TAG, "selectedAnimationMode=$selectedAnimationMode")
                }

                check(R.id.rb_animation_mode_fade)
            }
        }

    private fun onAnimationModeChanged(checkedId: Int) {
        when (checkedId) {
            R.id.rb_animation_mode_slide -> selectedAnimationMode = Snackbar.ANIMATION_MODE_SLIDE
            R.id.rb_animation_mode_fade -> selectedAnimationMode = Snackbar.ANIMATION_MODE_FADE
        }
    }

    private fun showShortSnackbar() =
        view?.let {
            setupSnackbar(Snackbar.make(it, snackbarText, Snackbar.LENGTH_SHORT)).show()
        }

    private fun showLongSnackbar() =
        view?.let {
            setupSnackbar(Snackbar.make(it, snackbarText, Snackbar.LENGTH_LONG)).show()
        }

    private fun showIndefiniteSnackbar() =
        view?.let {
            with (Snackbar.make(it, snackbarText, Snackbar.LENGTH_INDEFINITE)) {
                setupSnackbar(this).setAction(R.string.dismiss) { dismiss() }.show()
            }
        }

    private fun setupSnackbar(snackbar: Snackbar): Snackbar = snackbar.apply {
        animationMode = selectedAnimationMode

        addCallback(object: Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                Log.d(TAG, "Snackbar dismissed")
                super.onDismissed(transientBottomBar, event)
            }

            override fun onShown(sb: Snackbar?) {
                Log.d(TAG, "Snackbar shown")
                super.onShown(sb)
            }
        })
    }
}