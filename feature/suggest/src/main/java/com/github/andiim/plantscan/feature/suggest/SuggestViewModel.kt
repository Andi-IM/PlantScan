package com.github.andiim.plantscan.feature.suggest

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.data.repository.SuggestionRepository
import com.github.andiim.plantscan.core.domain.GetUserLoginInfoUseCase
import com.github.andiim.plantscan.feature.suggest.navigation.SuggestArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SuggestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserDataUseCase: GetUserLoginInfoUseCase,
    private val repository: SuggestionRepository,
) : ViewModel() {
    private val args = SuggestArgs(savedStateHandle)
    val det = args.plantId

    val userData = getUserDataUseCase().filterNotNull().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = Status.Loading,
    )

    fun cancelUpload() {
        repository.cancelUpload()
    }
}

sealed interface Status {
    data object Loading : Status
    data class Granted(val id: String) : Status
    data object Denied : Status
}
