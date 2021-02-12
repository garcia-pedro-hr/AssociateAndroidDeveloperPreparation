package com.phgarcia.aadp.workers

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.phgarcia.aadp.MyApplication
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * Saves a temporary image to a permanent file.
 * SaveImageToFileWorker will take input and output.
 * The input is a String stored with the key MyApplication.WORKER_DATA_KEY_IMAGE_URI.
 * The output will also be a String stored with the key MyApplication.WORKER_DATA_KEY_IMAGE_URI.
 */
class SaveImageToFileWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        Timber.d("doWork")

        val filename = System.currentTimeMillis().toString()

        val relativeLocation = Environment.DIRECTORY_PICTURES

        /* Insertion of images should be performed using MediaColumns#IS_PENDING,
           which offers richer control over lifecycle. */
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val externalContentUri = applicationContext.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        return try {
            val resourceUri =
                Uri.parse(inputData.getString(MyApplication.WORKER_DATA_KEY_IMAGE_URI))

            val bitmap = BitmapFactory.decodeStream(
                applicationContext.contentResolver.openInputStream(resourceUri)
            )

            externalContentUri?.let { uri ->
                applicationContext.contentResolver.openOutputStream(uri)?.let { stream ->
                    if (bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)) {
                        Timber.d("Image saved as $relativeLocation${File.pathSeparator}$filename")
                    } else throw IOException("Failed to save bitmap")
                } ?: throw IOException("Failed to get output stream")
            } ?: throw IOException("Failed to create new MediaStore record")

            val outputData =
                workDataOf(MyApplication.WORKER_DATA_KEY_IMAGE_URI to externalContentUri.toString())

            Result.success(outputData)
        } catch (exception: Exception) {
            externalContentUri?.let {
                applicationContext.contentResolver.delete(externalContentUri, null, null)
            }

            Timber.e(exception)
            Result.failure()
        } finally {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            }
        }
    }
}