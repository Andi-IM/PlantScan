package com.github.andiim.plantscan.app.core.domain.usecase.firebase_services

import android.content.Context
import com.github.andiim.plantscan.app.core.data.Resource
import kotlinx.coroutines.flow.Flow
import org.tensorflow.lite.Interpreter

interface MLService {
    fun getModel(context: Context, modelName: String): Flow<Resource<Interpreter>>
}