package com.github.andiim.plantscan.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.app.ui.MainActivityUiState.Loading
import com.github.andiim.plantscan.app.ui.MainActivityUiState.Success
import com.github.andiim.plantscan.core.data.repository.UserDataRepository
import com.github.andiim.plantscan.core.domain.GetUserLoginInfoUseCase
import com.github.andiim.plantscan.core.model.data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    userLogin: GetUserLoginInfoUseCase,
) : ViewModel() {
    val uiState = combine(userLogin(), userDataRepository.userData) { auth, repo ->
        Success(
            userData = UserData(
                isLogin = !auth.isAnonymous,
                userId = auth.userId,
                darkThemeConfig = repo.darkThemeConfig,
                useDynamicColor = repo.useDynamicColor,
                shouldHideOnboarding = repo.shouldHideOnboarding,
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}
