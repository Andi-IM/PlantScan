package com.github.andiim.orchidscan.app.ui.screens.home.findPlant

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.andiim.orchidscan.app.data.firebase.LogService
import com.github.andiim.orchidscan.app.data.firebase.PlantDatabase
import com.github.andiim.orchidscan.app.data.model.Plant
import com.github.andiim.orchidscan.app.ui.screens.viewModels.PlantScanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(FlowPreview::class)
@HiltViewModel
class FindPlantViewModel @Inject constructor(
    private val plantDatabase: PlantDatabase,
    logService: LogService
) :
    PlantScanViewModel(logService) {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _fetchedData = MutableStateFlow<PagingData<Plant>>(PagingData.empty())
    val fetchedData: StateFlow<PagingData<Plant>> = _fetchedData.asStateFlow()

    fun onQueryChange(query: String) {
        _query.value = query
    }

    init {
        viewModelScope.launch {
            _query
                .debounce(1.seconds)
                .collect { query -> searchPlant(query) }
        }
    }

    private fun searchPlant(query: String) {
        viewModelScope.launch {
            if (query.isNotEmpty())
                plantDatabase
                    .searchPlant(query = query)
                    .cachedIn(viewModelScope)
                    .collect { _fetchedData.value = it }
            else _fetchedData.value = PagingData.empty()
        }
    }
}