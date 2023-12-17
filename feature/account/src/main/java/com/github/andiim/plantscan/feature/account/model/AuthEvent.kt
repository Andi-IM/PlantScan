package com.github.andiim.plantscan.feature.account.model

import com.github.andiim.plantscan.core.designsystem.component.textfield.UiText

data class AuthState(
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val repeatPassword: String = "",
    val passwordError: UiText? = null,
    val isVisiblePassword: Boolean = false,
    val eventAction: EventAction = EventAction.SIGN_IN,
)

enum class EventAction {
    SIGN_IN, SIGN_UP, FORGOT_PASSWORD
}

sealed interface AuthEvent {
    data class EmailChanged(val email: String) : AuthEvent
    data class PasswordChanged(val password: String) : AuthEvent
    data class RepeatPasswordChanged(val repeatPassword: String) : AuthEvent
    data class EventActionChanged(val eventAction: EventAction) : AuthEvent
    data class VisiblePassword(val isVisiblePassword: Boolean) : AuthEvent
    data object Submit : AuthEvent
}

data class AuthStatus(
    val loading: Boolean = false,
    val sent: Boolean = false,
    val granted: Boolean = false,
    val message: UiText? = null,
    val error: Boolean = false,
    val errorMessage: UiText? = null,
)
