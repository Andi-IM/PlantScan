package com.github.andiim.plantscan.core.domain

import com.github.andiim.plantscan.core.data.repository.SearchContentsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A use case which returns total count of *Fts tables.
 */
class GetSearchContentsCountUseCase @Inject constructor(
    private val searchContentRepository: SearchContentsRepository,
) {
    operator fun invoke(): Flow<Int> =
        searchContentRepository.getSearchContentsCount()
}
