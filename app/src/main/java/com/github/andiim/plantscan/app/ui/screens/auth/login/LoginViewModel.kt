package com.github.andiim.plantscan.app.ui.screens.auth.login

import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.AccountService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarManager
import com.github.andiim.plantscan.app.ui.navigation.Direction
import com.github.andiim.plantscan.app.PlantScanViewModel
import com.github.andiim.plantscan.library.android.extensions.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class LoginViewModel
@Inject
constructor(private val accountService: AccountService, logService: LogService) :
    PlantScanViewModel(logService) {
  private val _state = MutableStateFlow(LoginState())
  val state = _state.asStateFlow()

  fun onEmailChange(newValue: String) {
    _state.value = _state.value.copy(email = newValue)
  }

  fun onPasswordChange(newValue: String) {
    _state.value = _state.value.copy(password = newValue)
  }

  fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
    if (!_state.value.email.isValidEmail()) {
      SnackbarManager.showMessage(R.string.email_error)
      return
    }

    if (_state.value.password.isBlank()) {
      SnackbarManager.showMessage(R.string.error_empty_password)
      return
    }

    launchCatching {
      accountService.authenticate(_state.value.email, _state.value.password)
      openAndPopUp(Direction.Settings.route, Direction.Login.route)
    }
  }

  fun onForgotPasswordClick() {
    if (!_state.value.email.isValidEmail()) {
      SnackbarManager.showMessage(R.string.email_error)
      return
    }

    launchCatching {
      accountService.sendRecoveryEmail(_state.value.email)
      SnackbarManager.showMessage(R.string.hint_recovery_email_sent)
    }
  }
}
