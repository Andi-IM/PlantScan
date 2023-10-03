package com.github.andiim.plantscan.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.core.auth.AccountService
import com.github.andiim.plantscan.app.core.domain.usecase.firebaseServices.LogService
import com.github.andiim.plantscan.app.ui.common.extensions.isValidEmail
import com.github.andiim.plantscan.app.ui.common.extensions.isValidPassword
import com.github.andiim.plantscan.app.ui.common.extensions.launchCatching
import com.github.andiim.plantscan.app.ui.common.extensions.passwordMatches
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject
constructor(
    private val accountService: AccountService,
    private val logService: LogService,
) : ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state = _state.asStateFlow()

    fun onEmailChange(newValue: String) {
        _state.value = _state.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _state.value = _state.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        _state.value = _state.value.copy(repeatPassword = newValue)
    }

    fun onSignInClick(openFromLogin: () -> Unit) {
        if (!_state.value.email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (_state.value.password.isBlank()) {
            SnackbarManager.showMessage(R.string.error_empty_password)
            return
        }

        launchCatching(logService) {
            accountService.authenticate(_state.value.email, _state.value.password)
            openFromLogin.invoke()
        }
    }

    fun onSignUpClick(openFromSignUp: () -> Unit) {
        if (!_state.value.email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (!_state.value.password.isValidPassword()) {
            SnackbarManager.showMessage(R.string.error_wrong_password)
            return
        }

        if (!_state.value.password.passwordMatches(_state.value.repeatPassword)) {
            SnackbarManager.showMessage(R.string.error_password_match)
            return
        }

        launchCatching(logService) {
            accountService.linkAccount(_state.value.email, _state.value.password)
            openFromSignUp.invoke()
        }
    }

    fun onForgotPasswordClick() {
        if (!_state.value.email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        launchCatching(logService) {
            accountService.sendRecoveryEmail(_state.value.email)
            SnackbarManager.showMessage(R.string.hint_recovery_email_sent)
        }
    }
}
