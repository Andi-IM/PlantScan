package com.github.andiim.plantscan.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.auth.AuthHelper
import com.github.andiim.plantscan.core.data.repository.UserDataRepository
import com.github.andiim.plantscan.core.designsystem.component.textfield.UiText
import com.github.andiim.plantscan.core.domain.GetUserLoginInfoUseCase
import com.github.andiim.plantscan.feature.account.extensions.isValidEmail
import com.github.andiim.plantscan.feature.account.model.AuthEvent
import com.github.andiim.plantscan.feature.account.model.AuthState
import com.github.andiim.plantscan.feature.account.model.AuthStatus
import com.github.andiim.plantscan.feature.account.model.EventAction.FORGOT_PASSWORD
import com.github.andiim.plantscan.feature.account.model.EventAction.SIGN_IN
import com.github.andiim.plantscan.feature.account.model.EventAction.SIGN_UP
import com.github.andiim.plantscan.feature.account.validators.ValidateEmailUseCase
import com.github.andiim.plantscan.feature.account.validators.ValidatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val authHelper: AuthHelper,
    private val getUserLoginInfoUseCase: GetUserLoginInfoUseCase,
) : ViewModel() {
    private val validateEmailUseCase = ValidateEmailUseCase()
    private val validatePasswordUseCase = ValidatePasswordUseCase()

    private val _authStatus: MutableStateFlow<AuthStatus> = MutableStateFlow(AuthStatus())
    val authStatus: StateFlow<AuthStatus> = _authStatus.asStateFlow()

    private val _formState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState())
    val formState: StateFlow<AuthState> = _formState.asStateFlow()

    fun onEvent(event: AuthEvent) {
        viewModelScope.launch {
            when (event) {
                is AuthEvent.EmailChanged -> {
                    _formState.value = _formState.value.copy(email = event.email)
                    validateEmail()
                    resetStatus()
                }

                is AuthEvent.PasswordChanged -> {
                    resetStatus()
                    _formState.value = _formState.value.copy(password = event.password)
                    validatePassword()
                    resetStatus()
                }

                is AuthEvent.RepeatPasswordChanged -> {
                    resetStatus()
                    _formState.value = _formState.value.copy(repeatPassword = event.repeatPassword)
                    validateRepeatPassword()
                    resetStatus()
                }

                is AuthEvent.VisiblePassword -> {
                    _formState.value =
                        _formState.value.copy(isVisiblePassword = event.isVisiblePassword)
                }

                is AuthEvent.EventActionChanged -> {
                    _formState.value = _formState.value.copy(eventAction = event.eventAction)
                }

                AuthEvent.Submit -> {
                    _authStatus.value = _authStatus.value.copy(loading = true)
                    when (_formState.value.eventAction) {
                        SIGN_IN -> {
                            if (validateEmail() && validatePassword()) signIn()
                        }

                        SIGN_UP -> {
                            if (validateEmail() && validatePassword()) signUp()
                        }

                        FORGOT_PASSWORD -> {
                            if (validateEmail()) onForgotPasswordClick()
                            resetForm()
                        }
                    }
                }
            }
        }
    }

    @Suppress("detekt:TooGenericExceptionCaught")
    private suspend fun signIn() {
        try {
            authHelper.authenticate(
                _formState.value.email,
                _formState.value.password,
            ).collect()

            val info = getUserLoginInfoUseCase().first()
            userDataRepository.setLoginInfo(
                userId = info.userId,
                isAnonymous = info.isAnonymous,
            )
            _authStatus.value = _authStatus.value.copy(loading = false, granted = true)
        } catch (e: Exception) {
            _authStatus.value = _authStatus.value.copy(
                loading = true,
                error = true,
                errorMessage = UiText.DynamicString(e.toString()),
            )
        }
    }

    @Suppress("detekt:TooGenericExceptionCaught")
    private suspend fun signUp() {
        try {
            authHelper.linkAccount(
                _formState.value.email,
                _formState.value.password,
            ).collect()
            val info = getUserLoginInfoUseCase().first()
            userDataRepository.setLoginInfo(
                userId = info.userId,
                isAnonymous = info.isAnonymous,
            )
            _authStatus.value = _authStatus.value.copy(loading = false, granted = true)
        } catch (e: Exception) {
            _authStatus.value = _authStatus.value.copy(
                loading = false,
                error = true,
                errorMessage = UiText.DynamicString(e.toString()),
            )
        }
    }

    private fun resetStatus() {
        _authStatus.value = AuthStatus()
    }

    private fun resetForm() {
        _formState.value = AuthState()
    }

    private fun validateEmail(): Boolean {
        val emailResult = validateEmailUseCase.execute(_formState.value.email)
        _formState.value = _formState.value.copy(emailError = emailResult.errorMessage)
        return emailResult.successful
    }

    private fun validatePassword(): Boolean {
        val passwordResult = validatePasswordUseCase.execute(_formState.value.password)
        _formState.value = _formState.value.copy(passwordError = passwordResult.errorMessage)
        return passwordResult.successful
    }

    private fun validateRepeatPassword(): Boolean {
        val passwordResult = validatePasswordUseCase.matches(
            _formState.value.password,
            _formState.value.repeatPassword,
        )
        _formState.value = _formState.value
        return passwordResult.successful
    }

    private fun onForgotPasswordClick() {
        if (!_formState.value.email.isValidEmail()) {
            _authStatus.value = _authStatus.value.copy(
                loading = false,
                error = true,
                errorMessage = UiText.StringResource(R.string.email_not_valid_error),
            )
            return
        }

        viewModelScope.launch {
            authHelper.sendRecoveryEmail(_formState.value.email).collect()
        }

        _authStatus.value = _authStatus.value.copy(
            loading = false,
            sent = true,
            message = UiText.StringResource(R.string.hint_recovery_email_sent),
        )
    }
}
