package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.model.data.ObjectDetection
import com.github.andiim.plantscan.core.network.PsNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface DetectRepository {
    fun detect(base64ImageData: String, confidence: Int = 40): Flow<ObjectDetection>
}

class JustOnlineDetectRepository @Inject constructor(
    private val network: PsNetworkDataSource,
) : DetectRepository {
    override fun detect(base64ImageData: String, confidence: Int): Flow<ObjectDetection> = flow {
        emit(network.detect(base64ImageData, confidence).asExternalModel())
    }
}
