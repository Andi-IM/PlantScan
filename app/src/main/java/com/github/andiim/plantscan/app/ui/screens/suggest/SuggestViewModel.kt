package com.github.andiim.plantscan.app.ui.screens.suggest

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.andiim.plantscan.app.core.auth.AccountService
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.Suggestion
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.ui.common.extensions.launchCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SuggestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCase: PlantUseCase,
    private val logService: LogService,
    private val auth: AccountService
) : ViewModel() {

    private val _suggestData = MutableStateFlow(Suggestion())
    val data = _suggestData.asStateFlow()

    private val suggestArgs: SuggestArgs = SuggestArgs(savedStateHandle)
    val plantId = suggestArgs.plantId

    private val _suggestUiState: MutableStateFlow<SuggestUiState> =
        MutableStateFlow(SuggestUiState.Initial)
    val uiState = _suggestUiState.asStateFlow()


    fun onDescriptionChange(newValue: String) {
        _suggestData.value = _suggestData.value.copy(description = newValue)
    }

    fun onImageSet(newValue: Bitmap) {
        _suggestData.value = _suggestData.value.copy(image = newValue)
    }

    fun upload() {
        launchCatching(logService) {
            val user = auth.currentUser.first()

            if (!user.isAnonymous) {
                val data = Suggestion(
                    userId = user.id,
                    description = _suggestData.value.description,
                    image = _suggestData.value.image
                )

                useCase.sendSuggestion(data).collectLatest {
                    when (it) {
                        is Resource.Error -> {
                            val message = it.message
                            _suggestUiState.update { SuggestUiState.Error(message) }
                        }

                        is Resource.Loading -> {
                            val progress = it.progress
                            _suggestUiState.update { SuggestUiState.Loading(progress) }
                        }

                        is Resource.Success -> _suggestUiState.update { SuggestUiState.Success }
                    }
                }
            }
        }
    }
}

sealed interface SuggestUiState {
    data object Success : SuggestUiState
    data class Error(val message: String? = null) : SuggestUiState
    data class Loading(val progress: String? = null) : SuggestUiState
    data object Initial : SuggestUiState
}