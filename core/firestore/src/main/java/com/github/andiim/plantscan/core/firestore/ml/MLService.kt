package com.github.andiim.plantscan.core.firestore.ml

import com.github.andiim.plantscan.core.common.result.Resource
import kotlinx.coroutines.flow.Flow
import org.tensorflow.lite.Interpreter

interface MLService {
  fun getModel(modelName: String): Flow<Resource<Interpreter>>
}
