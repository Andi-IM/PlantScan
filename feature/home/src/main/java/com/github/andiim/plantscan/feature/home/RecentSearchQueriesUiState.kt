package com.github.andiim.plantscan.feature.home

import com.github.andiim.plantscan.data.model.RecentSearchQuery

sealed interface RecentSearchQueriesUiState {
  data object Loading : RecentSearchQueriesUiState

  data class Success(
      val recentQueries: List<RecentSearchQuery> = emptyList(),
  ) : RecentSearchQueriesUiState
}
