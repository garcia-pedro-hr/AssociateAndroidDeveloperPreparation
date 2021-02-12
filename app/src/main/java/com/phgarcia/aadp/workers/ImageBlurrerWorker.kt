package com.phgarcia.aadp.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.phgarcia.aadp.MyApplication
import com.phgarcia.aadp.utils.getBlurred
import com.phgarcia.aadp.utils.writeToFile
import timber.log.Timber
import java.io.InputStream

/**
 * Blurs an image.
 */
class ImageBlurrerWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        val resourceUri = inputData.getString(MyApplication.WORKER_DATA_KEY_IMAGE_URI)
        val blurLevel = inputData.getInt(MyApplication.WORKER_DATA_KEY_BLUR_LV, 0)

        Timber.d("doWork: resourceUri=$resourceUri blurLevel=$blurLevel")

        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                Timber.e("Invalid input URI")
                throw IllegalArgumentException("Invalid input URI")
            }

            val inputStream: InputStream? =
                applicationContext.contentResolver.openInputStream(Uri.parse(resourceUri))

            val outputUri = BitmapFactory
                .decodeStream(inputStream)
                .getBlurred(applicationContext, blurLevel)
                .writeToFile(applicationContext)

            val outputData =
                workDataOf(MyApplication.WORKER_DATA_KEY_IMAGE_URI to outputUri.toString())

            Timber.d("Image blur output is $outputUri")
            Result.success(outputData)
        } catch (throwable: Throwable) {
            Timber.e(throwable, "Error applying blur")
            Result.failure()
        }

    }
}