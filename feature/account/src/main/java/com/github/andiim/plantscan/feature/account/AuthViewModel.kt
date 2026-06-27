package com.github.andiim.plantscan.feature.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.auth.AuthHelper
import com.github.andiim.plantscan.core.data.repository.UserDataRepository
import com.github.andiim.plantscan.core.domain.GetUserLoginInfoUseCase
import com.github.andiim.plantscan.feature.account.extensions.isValidEmail
import com.github.andiim.plantscan.feature.account.extensions.passwordMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val authHelper: AuthHelper,
    private val getUserLoginInfoUseCase: GetUserLoginInfoUseCase,
) : ViewModel() {
    var formUiState by mutableStateOf(AuthFormUiState())
        private set

    fun onEmailChange(newValue: String) {
        formUiState = formUiState.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        formUiState = formUiState.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        formUiState = formUiState.copy(repeatPassword = newValue)
    }

    @Suppress("detekt:TooGenericExceptionCaught")
    fun onSignInClick(onSuccess: () -> Unit, onError: (Any?) -> Unit) {
        if (!formUiState.email.isValidEmail()) {
            onError(R.string.email_error)
        }
        if (formUiState.password.isBlank()) {
            onError(R.string.error_empty_password)
        }
        viewModelScope.launch {
            try {
                authHelper.authenticate(formUiState.email, formUiState.password).collect()
                getUserLoginInfoUseCase().collectLatest { info ->
                    userDataRepository.setLoginInfo(
                        userId = info.userId,
                        isAnonymous = info.isAnonymous,
                    )
                }
                onSuccess()
            } catch (e: Exception) {
                onError(e.message)
            }
        }
    }

    @Suppress("detekt:TooGenericExceptionCaught")
    fun onSignUpClick(onSuccess: () -> Unit, onError: (Any?) -> Unit) {
        if (!formUiState.email.isValidEmail()) {
            onError(R.string.email_error)
        }
        if (formUiState.password.isBlank()) {
            onError(R.string.error_empty_password)
        }
        if (!formUiState.password.passwordMatches(formUiState.repeatPassword)) {
            onError(R.string.error_password_match)
        }

        viewModelScope.launch {
            try {
                authHelper.linkAccount(formUiState.email, formUiState.password).collect {
                    val info = getUserLoginInfoUseCase().first()
                    userDataRepository.setLoginInfo(
                        userId = info.userId,
                        isAnonymous = info.isAnonymous,
                    )
                }
                onSuccess()
            } catch (e: Exception) {
                onError(e.message)
            }
        }
    }

    fun onForgotPasswordClick(): Int {
        if (!formUiState.email.isValidEmail()) {
            return R.string.email_error
        }
        viewModelScope.launch {
            authHelper.sendRecoveryEmail(formUiState.email).collect()
        }
        return R.string.hint_recovery_email_sent
    }
}
