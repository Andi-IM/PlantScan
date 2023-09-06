package com.github.andiim.plantscan.app.ui.screens.auth.signUp

import androidx.lifecycle.ViewModel
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.core.auth.AccountService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.ui.common.extensions.isValidEmail
import com.github.andiim.plantscan.app.ui.common.extensions.isValidPassword
import com.github.andiim.plantscan.app.ui.common.extensions.launchCatching
import com.github.andiim.plantscan.app.ui.common.extensions.passwordMatches
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarManager
import com.github.andiim.plantscan.app.ui.navigation.Direction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject
constructor(
    private val accountService: AccountService,
    private val logService: LogService,
) :
    ViewModel() {

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

        launchCatching(logService) {
            accountService.linkAccount(email, password)
            openAndPopUp(Direction.Settings.route, Direction.SignUp.route)
        }
    }
}
