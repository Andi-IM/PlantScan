package com.github.andiim.plantscan.app.ui.screens.auth.signUp

import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.data.firebase.AccountService
import com.github.andiim.plantscan.app.data.firebase.LogService
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarManager
import com.github.andiim.plantscan.app.ui.screens.viewModels.PlantScanViewModel
import com.github.andiim.plantscan.library.android.extensions.isValidEmail
import com.github.andiim.plantscan.library.android.extensions.isValidPassword
import com.github.andiim.plantscan.library.android.extensions.passwordMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
@HiltViewModel
class SignUpViewModel @Inject
constructor(private val accountService: AccountService, logService: LogService) :
    PlantScanViewModel(logService) {

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
            openAndPopUp("mandatory", "mandatory")
        }
    }
}
