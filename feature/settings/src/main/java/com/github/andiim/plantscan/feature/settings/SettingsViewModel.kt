package com.github.andiim.plantscan.feature.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.auth.AuthHelper
import com.github.andiim.plantscan.core.data.repository.UserDataRepository
import com.github.andiim.plantscan.core.domain.GetUserLoginInfoUseCase
import com.github.andiim.plantscan.core.model.data.DarkThemeConfig
import com.github.andiim.plantscan.feature.settings.SettingsUiState.Loading
import com.github.andiim.plantscan.feature.settings.SettingsUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val authHelper: AuthHelper,
    getUserLoginInfoUseCase: GetUserLoginInfoUseCase,
) : ViewModel() {

    var showDialog by mutableStateOf(false)
        private set

    val settingsUiState = combine(
        getUserLoginInfoUseCase(),
        userDataRepository.userData,
    ) { remote, data ->
        Success(
            settings = UserEditableSettings(
                isLogin = !remote.isAnonymous,
                currentUser = remote.userId,
                useDynamicColor = data.useDynamicColor,
                darkThemeConfig = data.darkThemeConfig,
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = Loading,
    )

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            userDataRepository.setDarkThemeConfig(darkThemeConfig)
        }
    }

    fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        viewModelScope.launch {
            userDataRepository.setDynamicColorPreference(useDynamicColor)
        }
    }

    fun signOut() {
        showDialog = true
        viewModelScope.launch {
            authHelper.signOut().collect()
            userDataRepository.setLoginInfo()
            showDialog = false
        }
    }

    fun deleteAccount() {
        showDialog = true
        viewModelScope.launch {
            authHelper.deleteAccount().collect()
            userDataRepository.setLoginInfo()
            showDialog = false
        }
    }
}

/**
 * Represents the settings which the user can edit within the app.
 */
data class UserEditableSettings(
    val isLogin: Boolean,
    val currentUser: String? = null,
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
)

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}
