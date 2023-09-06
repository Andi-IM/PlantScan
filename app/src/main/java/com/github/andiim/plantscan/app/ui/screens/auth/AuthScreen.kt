package com.github.andiim.plantscan.app.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.ParagraphStyle
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
import com.github.andiim.plantscan.app.ui.common.composables.EmailField
import com.github.andiim.plantscan.app.ui.common.composables.PasswordField
import com.github.andiim.plantscan.app.ui.common.composables.RepeatPasswordField
import com.github.andiim.plantscan.app.ui.common.extensions.basicButton
import com.github.andiim.plantscan.app.ui.common.extensions.fieldModifier
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import com.github.andiim.plantscan.app.R.string as AppString

@Composable
fun AuthRoute(
    openWeb: (String) -> Unit,
    navigateFromLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val loginState: AuthUiState by viewModel.state.collectAsState()

    AuthScreen(
        uiState = loginState,
        openWeb = openWeb,
        navigateFromLogin = navigateFromLogin,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
        onSignInClick = viewModel::onSignInClick,
        onSignUpClick = viewModel::onSignUpClick,
        onForgotPasswordClick = viewModel::onForgotPasswordClick
    )
}

@Composable
fun AuthScreen(
    uiState: AuthUiState,
    openWeb: (String) -> Unit = {},
    navigateFromLogin: () -> Unit,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onRepeatPasswordChange: (String) -> Unit,
    onForgotPasswordClick: () -> Unit = {},
    onSignInClick: (() -> Unit) -> Unit = {},
    onSignUpClick: (() -> Unit) -> Unit = {}
) {
    var isSignup by remember { mutableStateOf(false) }

    val animatedResource: Int by animateIntAsState(
        if (isSignup) R.string.label_create_account else R.string.label_sign_in,
        label = "String Resources for Login"
    )

    Box(
        modifier = Modifier
            .padding(24.dp)
            .testTag("Login Content")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailField(
                value = uiState.email,
                onNewValue = onEmailChange,
                modifier = Modifier
                    .fieldModifier()
                    .semantics(true) { contentDescription = "Email Field" })
            PasswordField(
                value = uiState.password,
                onNewValue = onPasswordChange,
                modifier = Modifier
                    .fieldModifier()
                    .semantics(true) { contentDescription = "Password Field" })

            AnimatedVisibility(visible = !isSignup) {
                ForgotPasswordButton(onClick = onForgotPasswordClick)
            }

            AnimatedVisibility(visible = isSignup) {
                RepeatPasswordField(
                    value = uiState.repeatPassword,
                    onNewValue = onRepeatPasswordChange,
                    modifier = Modifier
                        .fieldModifier()
                        .semantics(true) {
                            contentDescription = "Repeat Password Field"
                        })
            }

            BasicButton(
                text = animatedResource,
                modifier = Modifier
                    .basicButton()
                    .semantics(true) { contentDescription = "Sign In Button" },
                action = {
                    if (!isSignup) onSignInClick(navigateFromLogin)
                    else onSignUpClick(navigateFromLogin)
                },
            )

            ChangerButton(isSignUp = isSignup, onClick = { isSignup = !isSignup })
        }
        TermsAndPrivacyStatementText(
            openWeb = openWeb,
            modifier =
            Modifier
                .align(Alignment.BottomCenter)
                .semantics(true) {
                    contentDescription = "Terms Label"
                })
    }
}


@Composable
private fun ChangerButton(isSignUp: Boolean, onClick: () -> Unit) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle()) {
            append(context.getString(if (isSignUp) R.string.have_account_question_label else R.string.no_account_question_label))
        }
        append(" ")
        withStyle(
            style = SpanStyle(
                color = (MaterialTheme.colorScheme).primary,
                fontWeight = FontWeight.Bold
            ),
        ) {
            append(context.getString(if (isSignUp) R.string.label_sign_in else R.string.sign_up))
        }
    }

    ClickableText(
        text = annotatedText,
        onClick = { onClick() },
        )
}


@Composable
private fun ForgotPasswordButton(onClick: () -> Unit) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textAlign = TextAlign.End)) {
            append(context.getString(AppString.login_forgot_password_label))
        }
    }

    ClickableText(
        text = annotatedText,
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),

        )
}

@Composable
private fun TermsAndPrivacyStatementText(
    modifier: Modifier = Modifier,
    openWeb: (String) -> Unit,
) {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle()) { append(stringResource(AppString.terms_label)) }
        pushStringAnnotation(tag = "URL_A", annotation = "support-orchid.web.app/terms.html")
        withStyle(
            style =
            SpanStyle(color = (MaterialTheme.colorScheme).primary, fontWeight = FontWeight.Bold)
        ) {
            append(" ${stringResource(AppString.terms_text_button)} ")
        }
        pop()
        append(" ${stringResource(AppString.and_separator)} ")
        pushStringAnnotation(tag = "URL_B", annotation = "support-orchid.web.app/privacy.html")
        withStyle(
            style =
            SpanStyle(color = (MaterialTheme.colorScheme).primary, fontWeight = FontWeight.Bold)
        ) {
            append(" ${stringResource(AppString.privacy_policy_text_button)} ")
        }
        pop()
    }

    ClickableText(
        text = annotatedText,
        modifier = modifier,
        style = TextStyle.Default.copy(textAlign = TextAlign.Center, fontSize = 13.sp),
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "URL_A", start = offset, end = offset)
                .firstOrNull()?.let {
                    openWeb(it.item)
                }

            annotatedText.getStringAnnotations(tag = "URL_B", start = offset, end = offset)
                .firstOrNull()?.let {
                    openWeb(it.item)
                }
        }
    )
}

@Preview
@Composable
private fun Preview_LoginContent() {
    PlantScanTheme {
        Surface {
            AuthScreen(
                uiState = AuthUiState(),
                openWeb = {},
                navigateFromLogin = {},
                onEmailChange = {},
                onPasswordChange = {},
                onRepeatPasswordChange = {},
                onSignInClick = {},
                onForgotPasswordClick = {}
            )
        }
    }
}
