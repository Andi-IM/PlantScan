package com.github.andiim.plantscan.feature.signup

import com.github.andiim.plantscan.core.common.base.PlantScanViewModel
import com.github.andiim.plantscan.domain.firebase_services.AccountService
import com.github.andiim.plantscan.domain.firebase_services.LogService
import com.github.andiim.plantscan.feature.settings.navigation.settingsRoute
import com.github.andiim.plantscan.feature.signup.navigation.signUpRoute
import com.github.andiim.plantscan.core.designsystem.extensions.isValidEmail
import com.github.andiim.plantscan.core.designsystem.extensions.isValidPassword
import com.github.andiim.plantscan.core.designsystem.extensions.passwordMatches
import com.github.andiim.plantscan.core.ui.snackbar.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class SignUpViewModel
@Inject
constructor(private val accountService: com.github.andiim.plantscan.domain.firebase_services.AccountService, logService: com.github.andiim.plantscan.domain.firebase_services.LogService) :
    com.github.andiim.plantscan.core.common.base.PlantScanViewModel(logService) {

  private val _state = MutableStateFlow(SignUpState())
  val state = _state.asStateFlow()

  private val email = _state.value.email
  private val password = _state.value.password

  fun onEmailChange(newValue: String) {
    _state.value = _state.value.copy(email = newValue)
  }

  fun onPasswordChange(newValue: String) {
    _state.value = _state.value.copy(password = newValue)
  }

  fun onRepeatPasswordChange(newValue: String) {
    _state.value = _state.value.copy(repeatPassword = newValue)
  }

  fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(R.string.email_error)
      return
    }

    if (!password.isValidPassword()) {
      SnackbarManager.showMessage(R.string.error_wrong_password)
      return
    }

    if (!password.passwordMatches(_state.value.repeatPassword)) {
      SnackbarManager.showMessage(R.string.error_password_match)
      return
    }

    launchCatching {
      accountService.linkAccount(email, password)
      openAndPopUp(com.github.andiim.plantscan.feature.settings.navigation.settingsRoute, signUpRoute)
    }
  }
}
