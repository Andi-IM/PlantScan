package com.github.andiim.plantscan.core.data.repository

import androidx.lifecycle.asFlow
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.github.andiim.plantscan.core.firestore.FirebaseDataSource
import com.github.andiim.plantscan.core.model.data.Suggestion
import com.github.andiim.plantscan.core.storageUpload.StorageHelper
import com.github.andiim.plantscan.core.workers.SuggestionImgUploadWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

interface SuggestionRepository {
    val outputWorkInfo: Flow<WorkInfo>
    fun uploadImageRequestBuilder(suggestion: Suggestion)
    fun cancelUpload(): Unit
}

class DefaultSuggestRepository @Inject constructor(
    private val workManager: WorkManager,
    firebase: FirebaseDataSource,
    storage: StorageHelper,
) : SuggestionRepository {
    override val outputWorkInfo: Flow<WorkInfo> =
        workManager.getWorkInfosByTagLiveData("").asFlow().mapNotNull {
            if (it.isNotEmpty()) it.first() else null
        }

    override fun uploadImageRequestBuilder(suggestion: Suggestion) {
        suggestion.imageBase64.takeIf { it.isNotEmpty() }?.let {
            var continuation = workManager.beginUniqueWork(
                "",
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.Companion.from(SuggestionImgUploadWorker::class.java),
            )

            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()

            continuation.enqueue()
        }
    }

    override fun cancelUpload() {
        workManager.cancelUniqueWork("")
    }

    private fun createInputDataForWorkRequest(suggestion: Suggestion): Data {
        val builder = Data.Builder()
        builder.putStringArray(
            SuggestionImgUploadWorker.BASE64_IMAGE_STRING_ARRAY,
            suggestion.imageBase64.toTypedArray(),
        )
        return builder.build()
    }
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
 * */
/*

    override fun cancelUpload() {
        workManager.cancelUniqueWork(IMAGE_UPLOAD_WORK_NAME)
    }


 */
/**
 * Creates the input data bundle which includes the Uri to operate on
 * @return Data which contains the Image Uri as a String
 */
/*

    private fun uriInputDataBuilder(uri: Uri): Data {
        return Data.Builder().putString(KEY_IMAGE_URI, uri.toString()).build()
    }
}*/
