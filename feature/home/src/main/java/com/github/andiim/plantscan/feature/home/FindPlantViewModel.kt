package com.github.andiim.plantscan.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import com.github.andiim.plantscan.core.crashlytics.LogService
import com.github.andiim.plantscan.core.crashlytics.PlantScanViewModel
import com.github.andiim.plantscan.data.repository.PlantRepository
import com.github.andiim.plantscan.data.repository.PlantRepository.Companion.getDefaultPageConfig
import com.github.andiim.plantscan.data.repository.PlantResourceQuery
import com.github.andiim.plantscan.data.repository.RecentSearchRepository
import com.github.andiim.plantscan.domain.GetSearchContentCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(FlowPreview::class)
@HiltViewModel
class FindPlantViewModel
@Inject
constructor(
    getSearchContentCountUseCase: GetSearchContentCountUseCase,
    private val recentSearchRepository: RecentSearchRepository,
    private val repository: PlantRepository,
    private val savedStateHandle: SavedStateHandle,
    logService: LogService,
) : PlantScanViewModel(logService) {

  val searchQuery = savedStateHandle.getStateFlow(SEARCH_QUERY, "")

  //    val searchResultUiState: StateFlow<SearchResultUiState> = x

  private val queryState = MutableStateFlow("")
  val query: StateFlow<String> = queryState.asStateFlow()

  val items =
      Pager(
              config = getDefaultPageConfig(),
              pagingSourceFactory = { repository.getPlants(PlantResourceQuery()) })
          .flow

  fun onQueryChange(queryData: String) {
    savedStateHandle[SEARCH_QUERY] = query
  }

  fun onSearchTriggered(query: String) {
    launchCatching { recentSearchRepository.insertOrReplaceRecentSearch(query) }
  }

  fun clearRecentSearches() {
    launchCatching { recentSearchRepository.clearRecentSearches() }
  }
}

/** Minimum length where search query is considered as [SearchResultUiState.EmptyQuery] */
private const val SEARCH_QUERY_MIN_LENGTH = 2

/** Minimum number of the fts table's entity count where it's considered as search is not ready */
private const val SEARCH_MIN_FTS_ENTITY_COUNT = 1
private const val SEARCH_QUERY = "searchQuery"
