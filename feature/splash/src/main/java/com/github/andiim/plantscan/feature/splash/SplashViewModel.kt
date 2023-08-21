package com.github.andiim.plantscan.feature.splash

import androidx.compose.runtime.mutableStateOf
import com.github.andiim.plantscan.core.crashlytics.LogService
import com.github.andiim.plantscan.core.crashlytics.PlantScanViewModel
import com.github.andiim.plantscan.data.repository.AccountRepository
import com.github.andiim.plantscan.data.repository.ConfigRepository
import com.github.andiim.plantscan.feature.splash.navigation.splashRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject
constructor(
  configurationService: ConfigRepository,
  private val account: AccountRepository,
  logService: LogService
) : PlantScanViewModel(logService) {
  val showError = mutableStateOf(false)

  init {
    launchCatching { configurationService.fetchConfiguration() }
  }

  fun onAppStart(openAndPopup: (String, String) -> Unit) {
    showError.value = false
    if (account.hasUser) openAndPopup("findPlantRoute", splashRoute)
    else {
      launchCatching(snackbar = true) {
        try {
          account.createAnonymousAccount()
        } catch (ex: Exception) {
          showError.value = true
          throw ex
        }
        openAndPopup("findPlantRoute", splashRoute)
      }
    }
  }
}
