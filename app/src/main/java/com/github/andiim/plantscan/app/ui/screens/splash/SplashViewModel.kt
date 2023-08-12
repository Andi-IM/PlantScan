package com.github.andiim.plantscan.app.ui.screens.splash

import androidx.compose.runtime.mutableStateOf
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.AccountService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.ConfigurationService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.ui.navigation.Direction
import com.github.andiim.plantscan.app.PlantScanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject
constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService,
    logService: LogService
) : PlantScanViewModel(logService) {
  val showError = mutableStateOf(false)

  init {
    launchCatching { configurationService.fetchConfiguration() }
  }

  fun onAppStart(openAndPopup: (String, String) -> Unit) {
    showError.value = false
    if (accountService.hasUser) openAndPopup(Direction.MainNav.route, Direction.Splash.route)
    else {
      launchCatching(snackbar = false) {
        try {
          accountService.createAnonymousAccount()
        } catch (ex: Exception) {
          showError.value = true
          throw ex
        }
        openAndPopup(Direction.MainNav.route, Direction.Splash.route)
      }
    }
  }
}
