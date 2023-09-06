package com.github.andiim.plantscan.app.ui.screens.home.settings

import androidx.lifecycle.ViewModel
import com.github.andiim.plantscan.app.core.auth.AccountService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.ui.common.extensions.launchCatching
import com.github.andiim.plantscan.app.ui.navigation.Direction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountService: AccountService,
    private val logService: LogService
) : ViewModel() {
    val uiState = accountService.currentUser.map { SettingsUiState(it.isAnonymous) }

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching(logService) { accountService.signOut() }
        restartApp(Direction.Splash.route)
    }

    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        launchCatching(logService) {
            accountService.deleteAccount()
            restartApp(Direction.Splash.route)
        }
    }
}