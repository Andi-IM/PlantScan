package com.github.andiim.plantscan.feature.findplant

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.andiim.core.analytics.AnalyticsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FindPlantViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {
    val searchQuery: StateFlow<String> = savedStateHandle.getStateFlow(SEARCH_QUERY, "")
}

private const val SEARCH_QUERY_MIN_LENGTH = 2
private const val SEARCH_MIN_FTS_ENTITY_COUNT = 1
private const val SEARCH_QUERY = "searchQuery"
