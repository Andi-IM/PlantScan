package com.github.andiim.plantscan.app.ui.screens.home.settings

import com.github.andiim.plantscan.app.PlantScanViewModel
import com.github.andiim.plantscan.app.core.auth.AccountService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.ui.navigation.Direction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) :
    PlantScanViewModel(logService) {
    val uiState = accountService.currentUser.map { SettingsUiState(it.isAnonymous) }

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching { accountService.signOut() }
        restartApp(Direction.Splash.route)
    }

    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.deleteAccount()
            restartApp(Direction.Splash.route)
        }
    }
}