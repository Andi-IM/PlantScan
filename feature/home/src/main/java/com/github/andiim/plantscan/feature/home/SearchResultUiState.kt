package com.github.andiim.plantscan.feature.home

import com.github.andiim.plantscan.model.data.Plant

interface SearchResultUiState {
  object Loading : SearchResultUiState

  /**
   * The state query is empty or too short. To distinguish the state between the (initial state or
   * when the search query is cleared) vs the state where no search result is returned, explicitly
   * define the empty query state.
   */
  object EmptyQuery : SearchResultUiState

  object LoadFailed : SearchResultUiState

  data class Success(
      val plants: List<Plant> = emptyList(),
  ) : SearchResultUiState {
    fun isEmpty(): Boolean = plants.isEmpty()
  }

  /**
   * A state where the search contents are not ready. This happens when the *Fts tables are not
   * populated yet.
   */
  object SearchNotReady : SearchResultUiState
}
