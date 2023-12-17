package com.github.andiim.plantscan.feature.account.validators

import com.github.andiim.plantscan.core.designsystem.component.textfield.UiText
import com.github.andiim.plantscan.feature.account.R
import com.github.andiim.plantscan.feature.account.extensions.isValidEmail
import com.github.andiim.plantscan.feature.account.model.ValidationResult

class ValidateEmailUseCase : BaseUseCase<String, ValidationResult> {
    override fun execute(input: String): ValidationResult {
        if (input.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.email_blank_error),
            )
        }
        if (!input.isValidEmail()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.email_not_valid_error),
            )
        }
        return ValidationResult(
            successful = true,
            errorMessage = null,
        )
    }
}
