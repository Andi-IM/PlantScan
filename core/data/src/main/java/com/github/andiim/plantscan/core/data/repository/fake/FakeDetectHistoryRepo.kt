package com.github.andiim.plantscan.core.data.repository.fake

import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.data.repository.DetectHistoryRepo
import com.github.andiim.plantscan.core.data.repository.DetectRepository
import com.github.andiim.plantscan.core.firestore.fake.FakeFirebaseDataSource
import com.github.andiim.plantscan.core.firestore.model.HistoryDocument
import com.github.andiim.plantscan.core.model.data.DetectionHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Fake implementation of the [DetectRepository].
 */
class FakeDetectHistoryRepo @Inject constructor(
    private val dataSource: FakeFirebaseDataSource,
) : DetectHistoryRepo {
    override fun recordDetection(detection: DetectionHistory): Flow<String> =
        flowOf("Success")

    override fun getDetectionHistories(userId: String): Flow<List<DetectionHistory>> = flow {
        emit(dataSource.getDetectionHistories(userId).map(HistoryDocument::asExternalModel))
    }
}
