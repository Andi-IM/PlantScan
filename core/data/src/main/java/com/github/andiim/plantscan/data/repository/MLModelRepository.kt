package com.github.andiim.plantscan.data.repository

import com.github.andiim.plantscan.core.common.result.Resource
import com.github.andiim.plantscan.core.firestore.ml.MLService
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import org.tensorflow.lite.Interpreter

interface MLModelRepository {
  fun getModel(modelName: String): Flow<Resource<Interpreter>>
}

class MLModelRepositoryImpl @Inject constructor(private val mlService: MLService) :
    MLModelRepository {
  override fun getModel(modelName: String): Flow<Resource<Interpreter>> =
      mlService.getModel(modelName)
}
