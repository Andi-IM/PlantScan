package com.github.andiim.plantscan.core.data.repository.fake

import androidx.work.WorkInfo
import com.github.andiim.plantscan.core.data.repository.SuggestionRepository
import com.github.andiim.plantscan.core.model.data.Suggestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeSuggestionRepository @Inject constructor() : SuggestionRepository {
    override val outputWorkInfo: Flow<WorkInfo>
        get() = flowOf()

    override fun uploadImageRequestBuilder(suggestion: Suggestion) = Unit

    override fun cancelUpload() = Unit
}
