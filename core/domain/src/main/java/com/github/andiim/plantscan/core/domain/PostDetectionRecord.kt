package com.github.andiim.plantscan.core.domain

import com.github.andiim.plantscan.core.data.repository.DetectHistoryRepo
import com.github.andiim.plantscan.core.model.data.DetectionHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostDetectionRecord @Inject constructor(
    private val historyRepo: DetectHistoryRepo,
) {
    operator fun invoke(history: DetectionHistory): Flow<String> =
        historyRepo.recordDetection(history)
}
