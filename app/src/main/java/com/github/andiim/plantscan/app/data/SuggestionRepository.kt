package com.github.andiim.plantscan.app.data

import android.app.Application
import android.net.Uri
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.github.andiim.plantscan.app.utils.KEY_IMAGE_URI
import com.github.andiim.plantscan.app.worker.FileUploadWorker
import javax.inject.Inject

interface SuggestionRepository {
    fun uploadImageRequestBuilder(uri: Uri?)
}

class DefaultSuggestionRepository @Inject constructor(
    application: Application
) : SuggestionRepository {
    private val workManager = WorkManager.getInstance(application)
    override fun uploadImageRequestBuilder(uri: Uri?) {
        uri?.let {
            val request =
                OneTimeWorkRequestBuilder<FileUploadWorker>().setInputData(uriInputDataBuilder(uri))
                    .build()
            workManager.enqueue(request)
        }
    }

    private fun uriInputDataBuilder(uri: Uri): Data {
        return Data.Builder().putString(KEY_IMAGE_URI, uri.toString()).build()
    }
}