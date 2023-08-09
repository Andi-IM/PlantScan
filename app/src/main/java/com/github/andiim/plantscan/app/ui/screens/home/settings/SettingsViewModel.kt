package com.github.andiim.plantscan.app.ui.screens.home.settings

import com.github.andiim.plantscan.app.data.firebase.AccountService
import com.github.andiim.plantscan.app.data.firebase.LogService
import com.github.andiim.plantscan.app.ui.navigation.Direction
import com.github.andiim.plantscan.app.ui.screens.viewModels.PlantScanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) :
    PlantScanViewModel(logService) {
    val uiState = accountService.currentUser.map { SettingsUiState(it.isAnonymous) }

    fun onLoginClick(openScreen: (String) -> Unit) {
        openScreen(Direction.Login.route)
    }

    fun onSignUpClick(openScreen: (String) -> Unit) {
        openScreen(Direction.SignUp.route)
    }

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