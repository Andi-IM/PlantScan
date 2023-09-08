package com.github.andiim.plantscan.app.ui.screens.home.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.app.core.auth.AccountService
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.github.andiim.plantscan.app.core.domain.model.User
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.ui.common.extensions.launchCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyGardenViewModel @Inject constructor(
    auth: AccountService,
    useCase: PlantUseCase,
    logService: LogService,
) : ViewModel() {

    private var currentUser: User? = null

    init {
        launchCatching(logService) {
            currentUser = auth.currentUser.first()
        }
    }

    val historyUiState: StateFlow<HistoryUiState> =
        historyUiState(
            user = currentUser,
            useCase = useCase
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HistoryUiState.Loading,
        )

}

private fun historyUiState(
    user: User?,
    useCase: PlantUseCase,
): Flow<HistoryUiState> {
    if (user == null || user.isAnonymous) return flowOf(HistoryUiState.Error("You must login to access this Feature!"))
    return useCase.getDetectionsList(user.id).map {
        when (it) {
            is Resource.Error -> HistoryUiState.Error(it.message)
            is Resource.Loading -> HistoryUiState.Loading
            is Resource.Success -> {
                val data = it.data
                HistoryUiState.Success(data)
            }
        }
    }

}

sealed interface HistoryUiState {
    data class Success(val detections: List<DetectionHistory>) : HistoryUiState
    data class Error(val message: String? = null) : HistoryUiState
    data object Loading : HistoryUiState

}