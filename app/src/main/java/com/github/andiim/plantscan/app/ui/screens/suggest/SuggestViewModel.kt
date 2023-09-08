package com.github.andiim.plantscan.app.ui.screens.suggest

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.app.core.auth.AccountService
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.Suggestion
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.ui.common.extensions.getImage
import com.github.andiim.plantscan.app.ui.common.extensions.launchCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SuggestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCase: PlantUseCase,
    private val logService: LogService,
    auth: AccountService
) : ViewModel() {

    private val suggestArgs: SuggestArgs = SuggestArgs(savedStateHandle)
    val plantId = suggestArgs.plantId

    val status: StateFlow<UiState> =
        userData(auth)
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                initialValue = UiState.Denied
            )


    private val _suggestData = MutableStateFlow(Suggestion())
    val data = _suggestData.asStateFlow()

    private val _sendingState: MutableStateFlow<SendingState> =
        MutableStateFlow(SendingState.Initial)
    val uiState = _sendingState.asStateFlow()


    fun onDescriptionChange(newValue: String) {
        _suggestData.value = _suggestData.value.copy(description = newValue)
    }

    fun onImageSet(context: Context, uris: List<Uri>, maxSize: Int) {
        if (uris.size <= maxSize) {
            val newValue: List<Bitmap> = uris.map { getImage(context, it) }
            _suggestData.value = _suggestData.value.copy(image = newValue)
        }
    }

    fun upload() {
        if (status.value is UiState.Granted) {
            val data = Suggestion(
                userId = (status.value as UiState.Granted).id,
                description = _suggestData.value.description,
                image = _suggestData.value.image
            )

            launchCatching(logService) {
                useCase.sendSuggestion(data).collectLatest {
                    when (it) {
                        is Resource.Error -> {
                            val message = it.message
                            _sendingState.update { SendingState.Error(message) }
                        }

                        is Resource.Loading -> {
                            val progress = it.progress
                            _sendingState.update { SendingState.Loading(progress) }
                        }

                        is Resource.Success -> _sendingState.update { SendingState.Success }
                    }
                }
            }
        }

    }
}

private fun userData(
    auth: AccountService,
): Flow<UiState> {
    return auth.currentUser.map {
        if (it.isAnonymous) UiState.Denied
        else UiState.Granted(it.id)
    }
}

sealed interface SendingState {
    data object Success : SendingState
    data class Error(val message: String? = null) : SendingState
    data class Loading(val progress: String? = null) : SendingState
    data object Initial : SendingState
}

sealed interface UiState {
    data class Granted(val id: String) : UiState
    data object Denied : UiState
}