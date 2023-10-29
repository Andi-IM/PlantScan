package com.github.andiim.plantscan.feature.findplant

import com.github.andiim.plantscan.core.data.model.RecentSearchQuery

sealed interface RecentSearchQueriesUiState {
    data object Loading : RecentSearchQueriesUiState
    data class Success(
        val recentQueries: List<RecentSearchQuery> = emptyList(),
    ) : RecentSearchQueriesUiState
}
