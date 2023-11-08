package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.data.model.asDocument
import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.firestore.FirebaseDataSource
import com.github.andiim.plantscan.core.model.data.ObjectDetection
import com.github.andiim.plantscan.core.model.data.Suggestion
import com.github.andiim.plantscan.core.network.PsNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface DetectRepository {
    fun detect(
        base64ImageData: String,
        confidence: Int = 40,
        overlap: Int = 30,
    ): Flow<ObjectDetection>

    fun sendSuggestion(suggestion: Suggestion): Flow<String>
}

class JustOnlineDetectRepository @Inject constructor(
    private val network: PsNetworkDataSource,
    private val firebase: FirebaseDataSource,
) : DetectRepository {
    override fun detect(
        base64ImageData: String,
        confidence: Int,
        overlap: Int,
    ): Flow<ObjectDetection> = flow {
        emit(network.detect(base64ImageData, confidence, overlap).asExternalModel())
    }

    override fun sendSuggestion(suggestion: Suggestion): Flow<String> = flow {
        emit(firebase.sendSuggestion(suggestion.asDocument()))
    }
}
