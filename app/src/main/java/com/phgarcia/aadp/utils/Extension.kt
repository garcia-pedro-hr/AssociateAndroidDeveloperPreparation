package com.phgarcia.aadp.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.WorkerThread

import com.ibm.icu.text.RuleBasedNumberFormat
import com.phgarcia.aadp.MyApplication
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * Converts an Int number into localized string representing it written in full
 * @param context A context
 * @return Number written in full as a string
 */
fun Int.writtenInFull(context: Context): String {
    val currentLocale = context.resources.configuration.locales[0]
    val rule = RuleBasedNumberFormat(currentLocale, RuleBasedNumberFormat.SPELLOUT)
    return rule.format(this)
}

/**
 * Blurs the given Bitmap image
 * @param context A context
 * @param blurLevel A multiplier for the blur radius.
 *  Since Radius range is 0 < r <= 25, blurLevel range is 1 <= bl <= 5
 * @return Blurred bitmap image
 */
@WorkerThread
fun Bitmap.getBlurred(context: Context, blurLevel: Int): Bitmap =
    if (blurLevel in 1..5) {
        lateinit var rsContext: RenderScript

        try {
            // Blur the image
            rsContext = RenderScript.create(context, RenderScript.ContextType.DEBUG)

            val inAlloc = Allocation.createFromBitmap(rsContext, this)
            val outAlloc = Allocation.createTyped(rsContext, inAlloc.type)

            ScriptIntrinsicBlur.create(rsContext, Element.U8_4(rsContext)).apply {
                setRadius(5f * blurLevel)
                setInput(inAlloc)
                forEach(outAlloc)
            }

            // Create the output bitmap and copy output allocation to it before returning
            Bitmap.createBitmap(width, height, config).apply {
                outAlloc.copyTo(this)
            }
        } finally {
            rsContext.finish()
        }
    } else {
        Timber.e("Blur level $blurLevel out of range (1 <= bl <= 5)")
        this
    }

/**
 * Writes bitmap to a temporary file and returns the Uri for the file
 * @param context Application context
 * @return Uri for temp file with bitmap
 * @throws FileNotFoundException Throws if bitmap file cannot be found
 */
@Throws(FileNotFoundException::class)
fun Bitmap.writeToFile(context: Context): Uri {
    val name = String.format("blur-filter-output-%s.png", UUID.randomUUID().toString())
    val outputDir = File(context.filesDir, MyApplication.BITMAP_OUTPUT_PATH)

    if (!outputDir.exists()) {
        outputDir.mkdirs() // should succeed
    }

    val outputFile = File(outputDir, name)
    var out: FileOutputStream? = null

    try {
        out = FileOutputStream(outputFile)
        compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, out)
    } finally {
        out?.let {
            try {
                it.close()
            } catch (ignore: IOException) {
            }
        }
    }

    return Uri.fromFile(outputFile)
}