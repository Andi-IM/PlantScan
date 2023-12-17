package com.github.andiim.plantscan.feature.account.model

import com.github.andiim.plantscan.core.designsystem.component.textfield.UiText

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: UiText? = null,
)

