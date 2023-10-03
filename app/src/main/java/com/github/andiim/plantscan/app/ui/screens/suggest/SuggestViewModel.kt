package com.github.andiim.plantscan.app.ui.screens.suggest

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.core.auth.AccountService
import com.github.andiim.plantscan.app.core.domain.model.Suggestion
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.ui.common.extensions.getImage
import com.github.andiim.plantscan.app.ui.common.extensions.launchCatching
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    val status: StateFlow<Status> =
        userData(auth)
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                initialValue = Status.Loading
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
        if (status.value is Status.Granted) {
            _sendingState.value = SendingState.Loading

            if (_suggestData.value.description.isBlank()) {
                SnackbarManager.showMessage(R.string.suggest_description_error_message)
                _sendingState.value = SendingState.Initial
                return
            }

            val data = Suggestion(
                userId = (status.value as Status.Granted).id,
                description = _suggestData.value.description,
                image = _suggestData.value.image
            )

            launchCatching(logService) {
                useCase.sendSuggestion(suggestion = data)
                    .catch { error ->
                        _sendingState.value = SendingState.Error(error.message)
                    }
                    .collectLatest {
                        _sendingState.value = SendingState.Success
                    }
            }
        }
    }
}

private fun userData(
    auth: AccountService,
): Flow<Status> {
    return auth.currentUser.map {
        if (it.isAnonymous) {
            Status.Denied
        } else Status.Granted(it.id)
    }
}

sealed interface SendingState {
    data object Initial : SendingState
    data object Loading : SendingState
    data object Success : SendingState
    data class Error(val message: String? = null) : SendingState
}

sealed interface Status {
    data object Loading : Status
    data class Granted(val id: String) : Status
    data object Denied : Status
}
