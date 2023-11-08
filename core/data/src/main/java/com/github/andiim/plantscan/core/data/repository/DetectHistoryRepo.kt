package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.data.model.asDocument
import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.firestore.FirebaseDataSource
import com.github.andiim.plantscan.core.firestore.model.HistoryDocument
import com.github.andiim.plantscan.core.model.data.DetectionHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface DetectHistoryRepo {
    fun recordDetection(detection: DetectionHistory): Flow<String>
    fun getDetectionHistories(userId: String): Flow<List<DetectionHistory>>
}

class DefaultDetectHistoryRepo @Inject constructor(
    private val firebase: FirebaseDataSource,
) : DetectHistoryRepo {
    override fun recordDetection(detection: DetectionHistory): Flow<String> = flow {
        emit(firebase.recordDetection(detection.asDocument()))
    }

    override fun getDetectionHistories(userId: String): Flow<List<DetectionHistory>> = flow {
        emit(firebase.getDetectionHistories(userId).map(HistoryDocument::asExternalModel))
    }
}
