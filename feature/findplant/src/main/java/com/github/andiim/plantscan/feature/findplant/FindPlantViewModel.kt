package com.github.andiim.plantscan.feature.findplant

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.analytics.AnalyticsEvent
import com.github.andiim.plantscan.core.analytics.AnalyticsEvent.Param
import com.github.andiim.plantscan.core.analytics.AnalyticsHelper
import com.github.andiim.plantscan.core.data.repository.RecentSearchRepository
import com.github.andiim.plantscan.core.domain.GetRecentSearchQueriesUseCase
import com.github.andiim.plantscan.core.domain.GetSearchContentsCountUseCase
import com.github.andiim.plantscan.core.domain.GetSearchContentsUseCase
import com.github.andiim.plantscan.core.result.Result
import com.github.andiim.plantscan.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindPlantViewModel @Inject constructor(
    getSearchContentsUseCase: GetSearchContentsUseCase,
    getSearchContentsCountUseCase: GetSearchContentsCountUseCase,
    recentSearchQueriesUseCase: GetRecentSearchQueriesUseCase,
    private val recentSearchRepository: RecentSearchRepository,
    private val savedStateHandle: SavedStateHandle,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {
    val searchQuery: StateFlow<String> = savedStateHandle.getStateFlow(SEARCH_QUERY, "")
    val searchResultUiState: StateFlow<SearchResultUiState> =
        getSearchContentsCountUseCase().flatMapLatest { totalCount ->
            if (totalCount < SEARCH_MIN_FTS_ENTITY_COUNT) {
                flowOf(SearchResultUiState.SearchNotReady)
            } else {
                searchQuery.flatMapLatest { query ->
                    if (query.length < SEARCH_QUERY_MIN_LENGTH) {
                        flowOf(SearchResultUiState.EmptyQuery)
                    } else {
                        getSearchContentsUseCase(query).asResult().map {
                            when (it) {
                                is Result.Success -> {
                                    SearchResultUiState.Success(
                                        plants = it.data.plants,
                                    )
                                }

                                Result.Loading -> SearchResultUiState.Loading
                                is Result.Error -> SearchResultUiState.LoadFailed
                            }
                        }
                    }
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = SearchResultUiState.Loading,
        )
    val recentSearchQueriesUiState: StateFlow<RecentSearchQueriesUiState> =
        recentSearchQueriesUseCase().map(RecentSearchQueriesUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5_000),
                initialValue = RecentSearchQueriesUiState.Loading,
            )

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    /**
     * Called when the search option is explicitly triggered by the user. For example, when the
     * search icon is tapped in the IME or when the enter key is pressed in the search text field.
     *
     * The search results are displayed on the fly as the user types, but to explicitly save the
     * search query in the text field, defining this method.
     *
     * @param query the invoke string
     */
    fun onSearchTriggered(query: String) {
        viewModelScope.launch {
            recentSearchRepository.insertOrReplaceRecentSearch(query)
        }
        analyticsHelper.logEvent(
            AnalyticsEvent(
                type = SEARCH_QUERY,
                extras = listOf(
                    Param(SEARCH_QUERY, query),
                ),
            ),
        )
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            recentSearchRepository.clearRecentSearches()
        }
    }
}

private const val SEARCH_QUERY_MIN_LENGTH = 2
private const val SEARCH_MIN_FTS_ENTITY_COUNT = 1
private const val SEARCH_QUERY = "searchQuery"
