package com.github.andiim.plantscan.core.firestore.ml

import com.github.andiim.plantscan.core.common.result.Resource
import com.github.andiim.plantscan.core.firestore.services.trace
import com.google.firebase.ktx.Firebase
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.ktx.modelDownloader
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.tensorflow.lite.Interpreter

class MLServiceImpl @Inject constructor() : MLService {
  private val modelDownloader
    get() = Firebase.modelDownloader

  private fun checkCondition(): CustomModelDownloadConditions {
    return CustomModelDownloadConditions.Builder().requireWifi().build()
  }

  override fun getModel(
      modelName: String,
  ): Flow<Resource<Interpreter>> = flow {
    try {
      trace(DOWNLOAD_TRACE) {
        val customModel =
            modelDownloader
                .getModel(
                    modelName,
                    DownloadType.LATEST_MODEL,
                    checkCondition(),
                )
                .await()
        if (customModel.file != null) {
          val interpreter = Interpreter(customModel.file!!)
          emit(Resource.Success(data = interpreter))
        }
        emit(Resource.Error(exception = NullPointerException("File is not found!")))
      }
    } catch (e: Exception) {
      emit(Resource.Error(exception = e))
    }
  }

  companion object {
    const val DOWNLOAD_TRACE = "fetchModel"
  }
}
