package com.github.andiim.plantscan.feature.account.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.andiim.plantscan.core.designsystem.component.textfield.PsTextField
import com.github.andiim.plantscan.core.designsystem.component.textfield.UiText
import com.github.andiim.plantscan.core.designsystem.extensions.withSemantics
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordBlock(
    @StringRes label: Int,
    password: String,
    isVisiblePassword: Boolean,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    imeAction: ImeAction = ImeAction.Next,
    passwordError: UiText?,
    passwordTextFieldTag: String = "",
    passwordVisibilityTag: String = "",
) {
    Text(
        text = stringResource(id = label),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 18.dp),
    )
    PsTextField(
        placeholder = stringResource(id = label),
        text = password,
        onValueChange = onPasswordChange,
        keyboardType = KeyboardType.Password,
        imeAction = imeAction,
        trailingIcon = {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                IconButton(
                    onClick = onPasswordVisibilityChange,
                    modifier = Modifier.withSemantics(passwordVisibilityTag),
                ) {
                    Icon(
                        if (isVisiblePassword) PsIcons.Visible else PsIcons.InVisible,
                        contentDescription = if (isVisiblePassword) "Visible" else "InVisible",
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        },
        errorMessage = passwordError,
        isVisible = isVisiblePassword,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        singleLine = true,
        isError = passwordError != null,
        testTag = passwordTextFieldTag,
    )
}
