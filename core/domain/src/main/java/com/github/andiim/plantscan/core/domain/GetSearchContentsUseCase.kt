package com.github.andiim.plantscan.core.domain

import com.github.andiim.plantscan.core.data.repository.SearchContentsRepository
import com.github.andiim.plantscan.core.model.data.SearchResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchContentsUseCase @Inject constructor(
    private val searchContentsRepository: SearchContentsRepository
) {
    operator fun invoke(
        searchQuery: String,
    ): Flow<SearchResult> = searchContentsRepository.searchContents(searchQuery)
}
