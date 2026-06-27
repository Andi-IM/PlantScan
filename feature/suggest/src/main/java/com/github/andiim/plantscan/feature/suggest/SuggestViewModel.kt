package com.github.andiim.plantscan.feature.suggest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.domain.GetUserLoginInfoUseCase
import com.github.andiim.plantscan.core.domain.PostSuggestionUseCase
import com.github.andiim.plantscan.core.model.data.Suggestion
import com.github.andiim.plantscan.core.storageUpload.StorageHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

private const val TAG = "SuggestViewModel"

@HiltViewModel
class SuggestViewModel @Inject constructor(
    getUserDataUseCase: GetUserLoginInfoUseCase,
    private val postSuggestion: PostSuggestionUseCase,
    private val storageHelper: StorageHelper,
) : ViewModel() {
    var showDialog by mutableStateOf(false)
        private set

    var suggestState by mutableStateOf(Suggestion())
        private set

    val userData = getUserDataUseCase().map {
        if (it.isAnonymous) {
            Status.Denied
        } else {
            Status.Granted(it.userId)
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Status.Loading,
        )

    private val _sendSuggestState = MutableStateFlow<SuggestUiState>(SuggestUiState.Default)
    val sendSuggestState = _sendSuggestState.asStateFlow()

    fun onDescriptionChange(newValue: String) {
        suggestState = suggestState.copy(description = newValue)
    }

    fun onImageSet(uris: List<Uri>, maxSize: Int) {
        if (uris.size <= maxSize) {
            val newValue: List<String> = uris.map(Uri::toString)
            suggestState = suggestState.copy(images = newValue)
        }
    }

    @Suppress("detekt:TooGenericExceptionCaught")
    fun sendSuggestion(context: Context, onError: (String) -> Unit) {
        if (userData.value is Status.Granted) {
            if (suggestState.description.isBlank()) {
                onError(context.resources.getString(R.string.suggest_description_error_message))
                return
            }

            val id = (userData.value as Status.Granted).id
            viewModelScope.launch {
                try {
                    showDialog = true
                    with(suggestState) {
                        _sendSuggestState.value = SuggestUiState.Loading
                        val contentResolver = context.contentResolver
                        val listUrl: List<String> = if (images.isNotEmpty()) {
                            val images: List<Bitmap> = images.map {
                                BitmapFactory.decodeStream(
                                    contentResolver.openInputStream(
                                        Uri.parse(it),
                                    ),
                                )
                            }
                            images.mapIndexed { index, bitmap ->
                                val baseLocation =
                                    "suggestions/$id/${Clock.System.now()}_${id}_$index"
                                storageHelper.upload(bitmap, baseLocation).first()
                            }
                        } else {
                            listOf()
                        }

                        val result = postSuggestion(this.copy(images = listUrl)).first()

                        showDialog = false
                        _sendSuggestState.value = SuggestUiState.Complete
                        Log.i(TAG, "sendSuggestion: $result")
                        suggestState = Suggestion()
                    }
                } catch (throwable: Throwable) {
                    showDialog = false
                    _sendSuggestState.value = SuggestUiState.Default
                    Log.e(TAG, "Error Occurred", throwable)
                    onError(throwable.message.orEmpty())
                }
            }
        }
    }
}

sealed interface Status {
    data object Loading : Status
    data class Granted(val id: String) : Status
    data object Denied : Status
}

sealed interface SuggestUiState {

    data object Default : SuggestUiState
    data object Loading : SuggestUiState
    data object Complete : SuggestUiState
}
