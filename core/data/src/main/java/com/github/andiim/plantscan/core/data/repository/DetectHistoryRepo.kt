package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.data.model.asDocument
import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.firestore.PsFirebaseDataSource
import com.github.andiim.plantscan.core.firestore.model.HistoryDocument
import com.github.andiim.plantscan.core.model.data.DetectionHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface DetectHistoryRepo {
    fun recordDetection(detection: DetectionHistory): Flow<String>
    fun getDetectionHistories(userId: String): Flow<List<DetectionHistory>>

    fun getDetectionDetail(historyId: String): Flow<DetectionHistory>
}

class DefaultDetectHistoryRepo @Inject constructor(
    private val firebase: PsFirebaseDataSource,
) : DetectHistoryRepo {
    override fun recordDetection(detection: DetectionHistory): Flow<String> = flow {
        emit(firebase.recordDetection(detection.asDocument()))
    }

    override fun getDetectionHistories(userId: String): Flow<List<DetectionHistory>> = flow {
        emit(firebase.getDetectionHistories(userId).map(HistoryDocument::asExternalModel))
    }

    override fun getDetectionDetail(historyId: String): Flow<DetectionHistory> = flow {
        emit(firebase.getDetectionDetail(historyId).asExternalModel())
    }
}
