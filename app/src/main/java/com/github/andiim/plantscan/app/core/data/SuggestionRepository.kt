package com.github.andiim.plantscan.app.core.data

import android.app.Application
import android.net.Uri
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.github.andiim.plantscan.app.core.utils.IMAGE_UPLOAD_WORK_NAME
import com.github.andiim.plantscan.app.core.utils.KEY_IMAGE_URI
import com.github.andiim.plantscan.app.core.utils.TAG_OUTPUT
import com.github.andiim.plantscan.app.core.worker.FileUploadWorker
import javax.inject.Inject

interface SuggestionRepository {
    fun uploadImageRequestBuilder(uri: Uri?)
    fun cancelUpload(): Unit
}

class DefaultSuggestionRepository @Inject constructor(
    application: Application
) : SuggestionRepository {
    private val workManager = WorkManager.getInstance(application)
    override fun uploadImageRequestBuilder(uri: Uri?) {
        uri?.let {
            val uploadBuilder = OneTimeWorkRequestBuilder<FileUploadWorker>()
            uploadBuilder.setInputData(uriInputDataBuilder(uri))

            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()
            uploadBuilder.setConstraints(constraints)
            uploadBuilder.addTag(TAG_OUTPUT)

            val continuation = workManager.beginUniqueWork(
                IMAGE_UPLOAD_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                uploadBuilder.build()
            )

            continuation.enqueue()
        }
    }

    /**
     * Cancel any ongoing WorkRequests
     * */
    override fun cancelUpload() {
        workManager.cancelUniqueWork(IMAGE_UPLOAD_WORK_NAME)
    }


    /**
     * Creates the input data bundle which includes the Uri to operate on
     * @return Data which contains the Image Uri as a String
     */
    private fun uriInputDataBuilder(uri: Uri): Data {
        return Data.Builder().putString(KEY_IMAGE_URI, uri.toString()).build()
    }
}