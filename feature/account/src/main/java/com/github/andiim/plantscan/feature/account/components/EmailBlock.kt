package com.github.andiim.plantscan.feature.account.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.andiim.plantscan.core.designsystem.component.textfield.PsTextField
import com.github.andiim.plantscan.core.designsystem.component.textfield.UiText
import com.github.andiim.plantscan.feature.account.R

@Composable
fun EmailBlock(
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: UiText?,
) {
    Text(
        text = stringResource(id = R.string.label_email),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 18.dp),
    )
    PsTextField(
        placeholder = stringResource(id = R.string.label_email),
        text = email,
        onValueChange = onEmailChange,
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        singleLine = true,
        isError = emailError != null,
        errorMessage = emailError,
        testTag = "emailTextField"
    )
}
