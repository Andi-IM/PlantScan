package com.github.andiim.plantscan.app.core.data.source.firebase

import android.content.Context
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.MLService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.trace
import com.google.firebase.ktx.Firebase
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.ktx.modelDownloader
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.tensorflow.lite.Interpreter
import timber.log.Timber

class MLServiceImpl @Inject constructor() : MLService {
  private val modelDownloader
    get() = Firebase.modelDownloader

  private fun checkCondition(): CustomModelDownloadConditions {
    return CustomModelDownloadConditions.Builder().requireWifi().build()
  }

  override fun getModel(context: Context, modelName: String): Flow<Resource<Interpreter>> =
      flow {
            trace(DOWNLOAD_TRACE) {
              val data =
                  modelDownloader.getModel(
                      modelName, DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, checkCondition())

              Timber.d("Size= ${data.result.size}")

              if (data.result.file == null) {
                when (data.result.downloadId < data.result.size) {
                  true -> {
                    val progress = data.result.downloadId * 100L / data.result.size
                    Timber.d("progress $progress%")
                  }
                  else -> {
                    if (data.isSuccessful) {
                      data.result?.file?.let { emit(Resource.Success(Interpreter(it))) }
                    } else {
                      data.exception?.localizedMessage?.let { emit(Resource.Error(it)) }
                    }
                  }
                }
              } else {
                emit(Resource.Success(Interpreter(data.result.file!!)))
              }
            }
          }
          .flowOn(Dispatchers.IO)

  companion object {
    const val DOWNLOAD_TRACE = "fetchModel"
  }
}
