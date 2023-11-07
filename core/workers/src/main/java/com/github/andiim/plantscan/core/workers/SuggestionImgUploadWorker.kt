package com.github.andiim.plantscan.core.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.andiim.plantscan.core.bitmap.asImageFromBase64
import com.github.andiim.plantscan.core.network.AppDispatchers
import com.github.andiim.plantscan.core.network.Dispatcher
import com.github.andiim.plantscan.core.storageUpload.StorageHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@HiltWorker
class SuggestionImgUploadWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    @Dispatcher(AppDispatchers.IO) private val ioDispatchers: CoroutineDispatcher,
    private val storageHelper: StorageHelper,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val img = inputData.getStringArray(BASE64_IMAGE_STRING_ARRAY)?.map(String::asImageFromBase64)
        return withContext(ioDispatchers) {
            return@withContext try {
                val listUrl = img?.map { storageHelper.upload(it, "").first() }
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

    companion object {
        const val BASE64_IMAGE_STRING_ARRAY = "base64_image_string_array"
    }
}
