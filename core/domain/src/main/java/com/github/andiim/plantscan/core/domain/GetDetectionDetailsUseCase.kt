package com.github.andiim.plantscan.core.domain

import com.github.andiim.plantscan.core.data.repository.DetectHistoryRepo
import com.github.andiim.plantscan.core.model.data.DetectionHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetectionDetailsUseCase @Inject constructor(
    private val repository: DetectHistoryRepo,
) {
    operator fun invoke(historyId: String): Flow<DetectionHistory> =
        repository.getDetectionDetail(historyId)
}
