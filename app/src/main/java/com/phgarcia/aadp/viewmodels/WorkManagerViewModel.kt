package com.phgarcia.aadp.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import androidx.work.*
import com.phgarcia.aadp.MyApplication
import com.phgarcia.aadp.workers.CleanupWorker
import com.phgarcia.aadp.workers.ImageBlurrerWorker
import com.phgarcia.aadp.workers.SaveImageToFileWorker
import timber.log.Timber

class WorkManagerViewModel(application: Application) : AndroidViewModel(application) {

    private val workManager = WorkManager.getInstance(application)

    // This transformation makes sure that whenever the current work Id changes the WorkInfo
    // the UI is listening to changes
    private val outputWorkInfos: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosByTagLiveData(MyApplication.TAG_OUTPUT)

    // This transformation will observe outputWorkInfos and update itself if the work
    // finished successfully
    private val _outputUri = MediatorLiveData<Uri>()
    internal val outputUri: LiveData<Uri> = _outputUri

    init {
        _outputUri.addSource(outputWorkInfos) { listOfWorkInfo ->
            // If there are no matching work info, do nothing
            if (listOfWorkInfo.isNullOrEmpty()) {
                return@addSource
            }

            val workInfo = listOfWorkInfo[0]

            // We only care about the one output status
            // Every continuation has only one worker tagged TAG_OUTPUT
            if (workInfo.state.isFinished) {
                workInfo.outputData.getString(MyApplication.WORKER_DATA_KEY_IMAGE_URI)?.let {
                    if (it.isNotEmpty()) {
                        Timber.d("Posting new output image URI: $it")
                        _outputUri.value = Uri.parse(it)
                    }
                }
            }
        }
    }

    internal fun cancelWork() {
        workManager.cancelUniqueWork(MyApplication.WORK_NAME_IMG_MANIPULATION)
    }

    /**
     * Creates the input data bundle which includes the Uri to operate on.
     * @param imageUri the image URI to be included in the input data.
     * @return Data which contains the Image Uri as a String.
     */
    private fun createInputDataForUri(imageUri: Uri?, blurLevel: Int): Data =
        Data.Builder().apply {
            putString(MyApplication.WORKER_DATA_KEY_IMAGE_URI, imageUri?.toString())
            putInt(MyApplication.WORKER_DATA_KEY_BLUR_LV, blurLevel)
        }.build()

    /**
     * Create the WorkRequest to apply the blur and save the resulting image.
     * @param blurLevel The amount to blur the image.
     */
    internal fun applyBlur(imageUri: Uri?, blurLevel: Int) {
        Timber.d("applyBlur: imageUri=$imageUri blurLevel=$blurLevel")

        val constraints = Constraints.Builder()
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val blurRequest = OneTimeWorkRequestBuilder<ImageBlurrerWorker>()
            .setInputData(createInputDataForUri(imageUri, blurLevel))
            .build()

        val saveRequest = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .setConstraints(constraints)
            .addTag(MyApplication.TAG_OUTPUT)
            .build()

        workManager
            .beginUniqueWork(
                MyApplication.WORK_NAME_IMG_MANIPULATION,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
            )
            .then(blurRequest)
            .then(saveRequest)
            .enqueue()
    }

}