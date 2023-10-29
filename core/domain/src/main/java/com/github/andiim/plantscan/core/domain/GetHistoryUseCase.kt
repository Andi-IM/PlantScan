package com.github.andiim.plantscan.core.domain

import com.github.andiim.plantscan.core.data.repository.DetectHistoryRepo
import com.github.andiim.plantscan.core.model.data.DetectionHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A use case which returns the history.
 */
class GetHistoryUseCase @Inject constructor(
    private val historyRepository: DetectHistoryRepo
) {
    operator fun invoke(id: String): Flow<List<DetectionHistory>> =
        historyRepository.getDetectionHistories(id)
}
