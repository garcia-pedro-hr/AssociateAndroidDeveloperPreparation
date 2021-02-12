package com.phgarcia.aadp.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.phgarcia.aadp.MyApplication
import timber.log.Timber
import java.io.File

/**
 * Cleans up temporary files generated during blurring process.
 */
class CleanupWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        Timber.d("doWork")

        return try {
            File(applicationContext.filesDir, MyApplication.BITMAP_OUTPUT_PATH)
                .takeIf { it.exists() }
                ?.listFiles()
                ?.forEach {
                    // Delete temporary file if it exists and is a PNG
                    if (it.name.isNotEmpty() && it.name.endsWith(".png")) {
                        val deleted = it.delete()
                        Timber.d("delete: name=${it.name} deleted=$deleted")
                    }
                }
            Result.success()
        } catch (exception: Exception) {
            Timber.e(exception)
            Result.failure()
        }
    }
}