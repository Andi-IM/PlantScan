package com.github.andiim.plantscan.feature.account

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
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
import com.github.andiim.plantscan.core.designsystem.component.PsBackground
import com.github.andiim.plantscan.core.designsystem.component.PsButton
import com.github.andiim.plantscan.core.designsystem.extensions.fieldModifier
import com.github.andiim.plantscan.core.designsystem.extensions.withSemantics
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.core.designsystem.theme.PsTheme
import com.github.andiim.plantscan.feature.account.components.EmailField
import com.github.andiim.plantscan.feature.account.components.PasswordField
import com.github.andiim.plantscan.feature.account.components.RepeatPasswordField
import kotlinx.coroutines.launch

@Composable
fun AuthRoute(
    onBackPressed: () -> Unit,
    authCallback: () -> Unit,
    onShowSnackbar: suspend (String, String?, SnackbarDuration?) -> Boolean,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    suspend fun onShowSnackbar(message: String) {
        onShowSnackbar(message, "Dismiss", SnackbarDuration.Short)
    }

    suspend fun onShowSnackbar(@StringRes res: Int) {
        onShowSnackbar(context.getString(res))
    }

    suspend fun onShowSnackbar(message: Any?) {
        when (message) {
            is Int -> onShowSnackbar(message)
            is String -> onShowSnackbar(message)
            else -> throw IllegalArgumentException("doesn't support this type!")
        }
    }

    AuthScreen(
        uiState = viewModel.formUiState,
        onLogin = {
            viewModel.onSignInClick(authCallback) {
                scope.launch { onShowSnackbar(it) }
            }
        },
        onSignUp = {
            viewModel.onSignUpClick(authCallback) {
                scope.launch { onShowSnackbar(it) }
            }
        },
        onBackPressed = onBackPressed,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onForgotPasswordClick = {
            scope.launch {
                onShowSnackbar(context.getString(viewModel.onForgotPasswordClick()))
            }
        },
        onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
    )
}

enum class AuthState {
    SIGN_IN, SIGN_UP, FORGOT_PASSWORD
}

@Suppress("detekt:LongMethod")
@Composable
fun AuthScreen(
    uiState: AuthFormUiState,
    onLogin: () -> Unit,
    onSignUp: () -> Unit,
    onBackPressed: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
) {
    var authState by remember { mutableStateOf(AuthState.SIGN_IN) }
    val submitTextValue = when (authState) {
        AuthState.SIGN_IN -> R.string.label_sign_in
        AuthState.SIGN_UP -> R.string.label_create_account
        AuthState.FORGOT_PASSWORD -> R.string.label_send_email_for_forgot_password
    }

    Box(Modifier.padding(vertical = 24.dp)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            EmailField(
                value = uiState.email,
                onNewValue = onEmailChange,
                modifier = Modifier
                    .fieldModifier()
                    .withSemantics("Email Field"),
            )

            AnimatedVisibility(visible = authState != AuthState.FORGOT_PASSWORD) {
                PasswordColumn(
                    password = uiState.password,
                    repeatPassword = uiState.repeatPassword,
                    state = authState,
                    onForgotPasswordClick = { authState = AuthState.FORGOT_PASSWORD },
                    onPasswordChange = onPasswordChange,
                    onRepeatPasswordChange = onRepeatPasswordChange,
                )
            }

            PsButton(
                text = submitTextValue,
                contentPadding = ButtonDefaults.TextButtonContentPadding,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    when (authState) {
                        AuthState.SIGN_IN -> onLogin()
                        AuthState.SIGN_UP -> onSignUp()
                        AuthState.FORGOT_PASSWORD -> {
                            onForgotPasswordClick()
                            authState = AuthState.SIGN_IN
                        }
                    }
                },
            )
            ChangerButton(
                state = authState,
                onClick = {
                    authState = when (authState) {
                        AuthState.SIGN_IN,
                        AuthState.FORGOT_PASSWORD,
                        -> AuthState.SIGN_UP

                        AuthState.SIGN_UP -> AuthState.SIGN_IN
                    }
                },
            )
        }
        Column(
            modifier = Modifier.align(Alignment.TopStart),
            horizontalAlignment = Alignment.Start,
        ) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape,
                    ),
            ) {
                Icon(PsIcons.Back, null, tint = Color.Black)
            }
        }
        TermsAndPrivacyStatementText(
            modifier =
            Modifier
                .align(Alignment.BottomCenter)
                .withSemantics(stringResource(R.string.terms_label_semantics)),
        )
    }
}

@Composable
fun PasswordColumn(
    password: String,
    repeatPassword: String,
    state: AuthState,
    onForgotPasswordClick: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
) {
    Column {
        PasswordField(
            value = password,
            onNewValue = onPasswordChange,
            modifier = Modifier
                .fieldModifier()
                .withSemantics("Password Field"),
        )
        AnimatedVisibility(visible = state == AuthState.SIGN_IN) {
            ForgotPasswordButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
            )
        }
        AnimatedVisibility(visible = state == AuthState.SIGN_UP) {
            RepeatPasswordField(
                value = repeatPassword,
                onNewValue = onRepeatPasswordChange,
                modifier = Modifier
                    .fieldModifier()
                    .withSemantics("Repeat Password Field"),
            )
        }
    }
}

@Composable
private fun ForgotPasswordButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textAlign = TextAlign.End)) {
            append(context.getString(R.string.login_forgot_password_label))
        }
    }

    ClickableText(
        text = annotatedText,
        style = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        onClick = { onClick() },
        modifier = modifier,
    )
}

@Composable
private fun ChangerButton(state: AuthState, onClick: () -> Unit) {
    val context = LocalContext.current
    val clickableText = when (state) {
        AuthState.SIGN_IN -> R.string.no_account_question_label
        else -> R.string.have_account_question_label
    }
    val label = when (state) {
        AuthState.SIGN_IN -> R.string.label_sign_in
        else -> R.string.label_sign_up
    }
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle()) { append(context.getString(clickableText)) }
        append(" ")
        withStyle(
            style =
            SpanStyle(color = (MaterialTheme.colorScheme).primary, fontWeight = FontWeight.Bold),
        ) {
            append(context.getString(label))
        }
    }

    ClickableText(
        text = annotatedText,
        style = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        onClick = { onClick() },
    )
}

private const val URI_A = "uri_a"
private const val URI_B = "uri_b"
private const val TERMS_URI = "https://support-orchid.web.app/terms.html"
private const val PRIVACY_URI = "https://support-orchid.web.app/privacy.html"

@Composable
private fun TermsAndPrivacyStatementText(
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle()) { append(stringResource(R.string.terms_label)) }
        pushStringAnnotation(tag = URI_A, annotation = TERMS_URI)
        withStyle(
            style =
            SpanStyle(color = (MaterialTheme.colorScheme).primary, fontWeight = FontWeight.Bold),
        ) {
            append(" ${stringResource(R.string.terms_text_button)} ")
        }
        pop()
        append(" ${stringResource(R.string.and_separator)} ")
        pushStringAnnotation(tag = URI_B, annotation = PRIVACY_URI)
        withStyle(
            style =
            SpanStyle(color = (MaterialTheme.colorScheme).primary, fontWeight = FontWeight.Bold),
        ) {
            append(" ${stringResource(R.string.privacy_policy_text_button)} ")
        }
        pop()
    }

    ClickableText(
        text = annotatedText,
        modifier = modifier,
        style = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface,
        ),
        onClick = { offset ->
            annotatedText
                .getStringAnnotations(tag = URI_A, start = offset, end = offset)
                .firstOrNull()
                ?.let { uriHandler.openUri(it.item) }

            annotatedText
                .getStringAnnotations(tag = URI_B, start = offset, end = offset)
                .firstOrNull()
                ?.let { uriHandler.openUri(it.item) }
        },
    )
}

@Preview
@Composable
private fun Preview_LoginContent() {
    PsTheme {
        PsBackground {
            AuthScreen(
                uiState = AuthFormUiState(),
                onEmailChange = {},
                onPasswordChange = {},
                onRepeatPasswordChange = {},
                onForgotPasswordClick = {},
                onBackPressed = {},
                onLogin = {},
                onSignUp = {},
            )
        }
    }
}
