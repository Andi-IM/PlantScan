package com.github.andiim.plantscan.feature.settings

import com.github.andiim.plantscan.core.common.base.PlantScanViewModel
import com.github.andiim.plantscan.domain.firebase_services.AccountService
import com.github.andiim.plantscan.domain.firebase_services.LogService
import com.github.andiim.plantscan.app.feature.login.navigation.loginRoute
import com.github.andiim.plantscan.app.feature.signUp.navigation.signUpRoute
import com.github.andiim.plantscan.feature.splash.navigation.splashRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class SettingsViewModel
@Inject
constructor(private val accountService: com.github.andiim.plantscan.domain.firebase_services.AccountService, logService: com.github.andiim.plantscan.domain.firebase_services.LogService) :
    com.github.andiim.plantscan.core.common.base.PlantScanViewModel(logService) {
  val uiState = accountService.currentUser.map { SettingsUiState(it.isAnonymous) }

  fun onLoginClick(openScreen: (String) -> Unit) {
    openScreen(loginRoute)
  }

  fun onSignUpClick(openScreen: (String) -> Unit) {
    openScreen(signUpRoute)
  }

  fun onSignOutClick(restartApp: (String) -> Unit) {
    launchCatching { accountService.signOut() }
    restartApp(com.github.andiim.plantscan.feature.splash.navigation.splashRoute)
  }

  fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
    launchCatching {
      accountService.deleteAccount()
      restartApp(com.github.andiim.plantscan.feature.splash.navigation.splashRoute)
    }
  }
}
