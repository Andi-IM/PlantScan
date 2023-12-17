package com.github.andiim.plantscan.feature.account.validators

import com.github.andiim.plantscan.core.designsystem.component.textfield.UiText
import com.github.andiim.plantscan.feature.account.R
import com.github.andiim.plantscan.feature.account.extensions.isValidPasswordLength
import com.github.andiim.plantscan.feature.account.extensions.passwordMatches
import com.github.andiim.plantscan.feature.account.model.ValidationResult

class ValidatePasswordUseCase : BaseUseCase<String, ValidationResult> {
    fun matches(input1: String, input2: String): ValidationResult {
        if (!input1.passwordMatches(input2)) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.error_password_match),
            )
        }
        return ValidationResult(
            successful = true,
        )
    }

    override fun execute(input: String): ValidationResult {
        /*if (!input.isValidPassword()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.error_wrong_password),
            )
        }*/

        if (!input.isValidPasswordLength()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.error_wrong_password_length),
            )
        }
        return ValidationResult(
            successful = true,
        )
    }
}
