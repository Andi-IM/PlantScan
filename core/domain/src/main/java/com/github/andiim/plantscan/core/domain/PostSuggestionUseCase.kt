package com.github.andiim.plantscan.core.domain

import com.github.andiim.plantscan.core.data.repository.DetectRepository
import com.github.andiim.plantscan.core.model.data.Suggestion
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostSuggestionUseCase @Inject constructor(
    private val detectRepository: DetectRepository,
) {
    operator fun invoke(suggestion: Suggestion): Flow<String> =
        detectRepository.sendSuggestion(suggestion)
}
