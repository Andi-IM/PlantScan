package com.github.andiim.plantscan.app.ui.screens.home.history

import androidx.lifecycle.ViewModel
import com.github.andiim.plantscan.app.core.auth.AccountService
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.ui.common.extensions.launchCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MyGardenViewModel @Inject constructor(
    private val useCase: PlantUseCase,
    private val auth: AccountService,
    private val logService: LogService,
) : ViewModel() {
    private val _historyUiState: MutableStateFlow<HistoryUiState> =
        MutableStateFlow(HistoryUiState.Loading)
    val historyUiState: StateFlow<HistoryUiState> = _historyUiState.asStateFlow()

    fun fetchHistory() {
        launchCatching(logService) {
            auth.currentUser.collectLatest { user ->
                if (user.isAnonymous)
                    _historyUiState.value =
                        HistoryUiState.Error("You must login to access this Feature!")
                else {
                    useCase.getDetectionsList(user.id).map {
                        when (it) {
                            is Resource.Error -> HistoryUiState.Error(it.message)
                            is Resource.Loading -> HistoryUiState.Loading
                            is Resource.Success -> {
                                val data = it.data
                                HistoryUiState.Success(data)
                            }
                        }
                    }.collectLatest { result ->
                        _historyUiState.value = result
                    }
                }
            }
        }
    }
}


sealed interface HistoryUiState {
    data class Success(val detections: List<DetectionHistory>) : HistoryUiState
    data class Error(val message: String? = null) : HistoryUiState
    data object Loading : HistoryUiState
}
