package com.github.andiim.plantscan.app.ui.screens.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.ui.common.composables.BasicButton
import com.github.andiim.plantscan.app.ui.common.composables.BasicTextButton
import com.github.andiim.plantscan.app.ui.common.composables.EmailField
import com.github.andiim.plantscan.app.ui.common.composables.PasswordField
import com.github.andiim.plantscan.app.ui.common.extensions.basicButton
import com.github.andiim.plantscan.app.ui.common.extensions.fieldModifier
import com.github.andiim.plantscan.app.ui.common.extensions.textButton
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme

@Composable
fun LoginScreen(
    openWeb: (String) -> Unit,
    openAndPopUp: (String, String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val loginState by viewModel.state.collectAsState()

    LoginContent(
        email = loginState.email,
        password = loginState.password,
        openWeb = openWeb,
        openAndPopUp = openAndPopUp,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSignInClick = viewModel::onSignInClick,
        onForgotPasswordClick = viewModel::onForgotPasswordClick
    )
}

@Composable
fun LoginContent(
    email: String = "",
    password: String = "",
    openWeb: (String) -> Unit = {},
    openAndPopUp: (String, String) -> Unit,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onSignInClick: ((String, String) -> Unit) -> Unit = {}
) {
    Box(modifier = Modifier.padding(24.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailField(email, onEmailChange, Modifier.fieldModifier())
            PasswordField(password, onPasswordChange, Modifier.fieldModifier())

            BasicButton(
                R.string.label_sign_in,
                Modifier.basicButton(),
                action = { onSignInClick(openAndPopUp) })

            BasicTextButton(
                R.string.hint_forgot_password,
                Modifier.textButton(),
                action = onForgotPasswordClick
            )
        }
        TermsAndPrivacyStatementText(
            openWeb = openWeb,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun TermsAndPrivacyStatementText(
    modifier: Modifier = Modifier,
    openWeb: (String) -> Unit,
) {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle()) { append("By continuing, you agree to our") }
        pushStringAnnotation(tag = "URL_A", annotation = "android.com")
        withStyle(
            style =
            SpanStyle(color = (MaterialTheme.colorScheme).primary, fontWeight = FontWeight.Bold)
        ) {
            append(" Terms ")
        }
        append(" and ")
        pushStringAnnotation(tag = "URL_B", annotation = "developer.android.com")
        withStyle(
            style =
            SpanStyle(color = (MaterialTheme.colorScheme).primary, fontWeight = FontWeight.Bold)
        ) {
            append(" Privacy Policy ")
        }
        pop()
    }

    ClickableText(
        text = annotatedText,
        modifier = modifier,
        style = TextStyle.Default.copy(textAlign = TextAlign.Center, fontSize = 13.sp)
    ) { offset ->
        val termsString =
            annotatedText
                .getStringAnnotations(tag = "URL_A", start = offset, end = offset)
                .firstOrNull()
        val privacyString =
            annotatedText
                .getStringAnnotations(tag = "URL_B", start = offset, end = offset)
                .firstOrNull()

        if (termsString != null && privacyString == null) {
            openWeb(termsString.item)
        } else privacyString?.let { openWeb(it.item) }
    }
}

@Preview
@Composable
private fun Preview_LoginContent() {
    PlantScanTheme {
        Surface {
            LoginContent(openAndPopUp = { _, _ -> })
        }
    }
}