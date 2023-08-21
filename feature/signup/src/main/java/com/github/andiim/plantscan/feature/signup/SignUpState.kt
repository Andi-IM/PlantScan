package com.github.andiim.plantscan.feature.signup

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)