package com.github.andiim.plantscan.feature.account

import androidx.annotation.StringRes

data class AuthFormUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val error: String? = null,
)

sealed interface AuthUiState {
    data object Loading : AuthUiState
    data class Error(
        val message: String? = null,
        @StringRes val resMessage: Int? = null,
    ) : AuthUiState

    data object Success : AuthUiState
}
