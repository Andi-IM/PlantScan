package com.github.andiim.orchidscan.app.ui.screens.viewModels

import com.github.andiim.orchidscan.app.R
import com.github.andiim.orchidscan.app.data.firebase.AccountService
import com.github.andiim.orchidscan.app.data.firebase.LogService
import com.github.andiim.orchidscan.app.ui.common.snackbar.SnackbarManager
import com.github.andiim.orchidscan.app.ui.navigation.Direction
import com.github.andiim.orchidscan.app.ui.screens.auth.login.LoginState
import com.github.andiim.orchidscan.library.android.extensions.isValidEmail
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel @Inject
constructor(private val accountService: AccountService, logService: LogService) :
    PlantScanViewModel(logService) {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val email
        get() = _state.value.email
    private val password
        get() = _state.value.password

    fun onEmailChange(newValue: String) {
        _state.value = _state.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _state.value = _state.value.copy(password = newValue)
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (password.isBlank()) {
            SnackbarManager.showMessage(R.string.error_empty_password)
            return
        }

        launchCatching {
            accountService.authenticate(email, password)
            openAndPopUp(Direction.Settings.route, Direction.Login.route)
        }
    }

    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        launchCatching {
            accountService.sendRecoveryEmail(email)
            SnackbarManager.showMessage(R.string.hint_recovery_email_sent)
        }
    }
}