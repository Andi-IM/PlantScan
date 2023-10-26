package com.github.andiim.plantscan.core.data.repository

import android.net.Uri

interface SuggestionRepository {
    fun uploadImageRequestBuilder(uri: Uri?)
    fun cancelUpload(): Unit
}

/*
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

    */
/**
     * Cancel any ongoing WorkRequests
     * *//*

    override fun cancelUpload() {
        workManager.cancelUniqueWork(IMAGE_UPLOAD_WORK_NAME)
    }


    */
/**
     * Creates the input data bundle which includes the Uri to operate on
     * @return Data which contains the Image Uri as a String
     *//*

    private fun uriInputDataBuilder(uri: Uri): Data {
        return Data.Builder().putString(KEY_IMAGE_URI, uri.toString()).build()
    }
}*/
