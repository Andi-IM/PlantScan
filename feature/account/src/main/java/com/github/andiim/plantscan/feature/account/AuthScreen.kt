package com.github.andiim.plantscan.feature.account

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
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
import com.github.andiim.plantscan.core.designsystem.component.MinimalDialog
import com.github.andiim.plantscan.core.designsystem.component.PsBackground
import com.github.andiim.plantscan.core.designsystem.component.PsButton
import com.github.andiim.plantscan.core.designsystem.component.textfield.UiText
import com.github.andiim.plantscan.core.designsystem.extensions.withSemantics
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.core.designsystem.theme.PsTheme
import com.github.andiim.plantscan.feature.account.components.EmailBlock
import com.github.andiim.plantscan.feature.account.components.PasswordBlock
import com.github.andiim.plantscan.feature.account.model.AuthEvent
import com.github.andiim.plantscan.feature.account.model.AuthState
import com.github.andiim.plantscan.feature.account.model.EventAction
import com.github.andiim.plantscan.feature.account.model.EventAction.FORGOT_PASSWORD
import com.github.andiim.plantscan.feature.account.model.EventAction.SIGN_IN
import com.github.andiim.plantscan.feature.account.model.EventAction.SIGN_UP

@Composable
fun AuthRoute(
    onBackPressed: () -> Unit,
    authCallback: () -> Unit,
    onShowSnackbar: suspend (String, String?, SnackbarDuration?) -> Boolean,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    suspend fun onShowSnackbar(uiText: UiText?) {
        val message = uiText?.asString(context).orEmpty()
        onShowSnackbar(message, "Dismiss", SnackbarDuration.Short)
    }

    val uiState by viewModel.formState.collectAsState()
    val status by viewModel.authStatus.collectAsState()

    LaunchedEffect(uiState.eventAction, status) {
        when (uiState.eventAction) {
            SIGN_IN, SIGN_UP -> {
                if (status.granted) {
                    authCallback()
                }
                if (status.error) {
                    if (status.errorMessage != null) {
                        onShowSnackbar(status.message)
                    }
                }
            }

            FORGOT_PASSWORD -> {
                if (status.sent) {
                    if (status.message != null) {
                        onShowSnackbar(status.message)
                    }
                }
                if (status.error) {
                    if (status.errorMessage != null) {
                        onShowSnackbar(status.message)
                    }
                }
            }
        }
    }

    AuthScreen(
        uiState = uiState,
        isDialogShow = status.loading,
        onSubmit = { viewModel.onEvent(AuthEvent.Submit) },
        onBackPressed = onBackPressed,
        onEmailChange = { viewModel.onEvent(AuthEvent.EmailChanged(it)) },
        onPasswordChange = { viewModel.onEvent(AuthEvent.PasswordChanged(it)) },
        onRepeatPasswordChange = { viewModel.onEvent(AuthEvent.RepeatPasswordChanged(it)) },
        onPasswordVisibilityChange = {
            viewModel.onEvent(AuthEvent.VisiblePassword(!(viewModel.formState.value.isVisiblePassword)))
        },
        onEventActionChange = { viewModel.onEvent(AuthEvent.EventActionChanged(it)) },
    )
}

@Suppress("detekt:LongMethod")
@Composable
fun AuthScreen(
    uiState: AuthState,
    isDialogShow: Boolean,
    onSubmit: () -> Unit,
    onBackPressed: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onEventActionChange: (EventAction) -> Unit,
) {
    val submitTextValue = when (uiState.eventAction) {
        SIGN_IN -> R.string.label_sign_in
        SIGN_UP -> R.string.label_create_account
        FORGOT_PASSWORD -> R.string.label_send_email_for_forgot_password
    }

    Box(Modifier.padding(vertical = 24.dp)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            EmailBlock(
                email = uiState.email,
                onEmailChange = onEmailChange,
                emailError = uiState.emailError,
            )
            AnimatedVisibility(visible = uiState.eventAction != FORGOT_PASSWORD) {
                PasswordColumnBlock(
                    uiState = uiState,
                    onPasswordVisibilityChange = onPasswordVisibilityChange,
                    onForgotPasswordClick = { onEventActionChange(FORGOT_PASSWORD) },
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
                onClick = onSubmit,
            )
            ChangerButton(
                state = uiState.eventAction,
                onClick = {
                    onEventActionChange(
                        when (uiState.eventAction) {
                            SIGN_IN, FORGOT_PASSWORD -> SIGN_UP
                            SIGN_UP -> SIGN_IN
                        },
                    )
                },
                modifier = Modifier.testTag("changeMode")
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
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .withSemantics(stringResource(R.string.terms_label_semantics)),
        )
        if (isDialogShow) {
            MinimalDialog(message = uiState.eventAction.name)
        }
    }
}

@Composable
fun PasswordColumnBlock(
    uiState: AuthState,
    onPasswordVisibilityChange: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
) {
    Column {
        PasswordBlock(
            label = R.string.label_password,
            password = uiState.password,
            isVisiblePassword = uiState.isVisiblePassword,
            onPasswordChange = onPasswordChange,
            onPasswordVisibilityChange = onPasswordVisibilityChange,
            passwordError = uiState.passwordError,
            passwordTextFieldTag = "passwordTextField",
            passwordVisibilityTag = "passwordVisibility"
        )

        AnimatedVisibility(visible = uiState.eventAction == SIGN_IN) {
            ForgotPasswordButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
            )
        }

        AnimatedVisibility(visible = uiState.eventAction == SIGN_UP) {
            PasswordBlock(
                label = R.string.label_repeat_password,
                password = uiState.repeatPassword,
                isVisiblePassword = uiState.isVisiblePassword,
                onPasswordChange = onRepeatPasswordChange,
                onPasswordVisibilityChange = onPasswordVisibilityChange,
                passwordError = uiState.passwordError,
                passwordTextFieldTag = "repeatPasswordTextField",
                passwordVisibilityTag = "repeatPasswordVisibility"
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
private fun ChangerButton(
    state: EventAction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val clickableText = when (state) {
        SIGN_IN -> R.string.no_account_question_label
        else -> R.string.have_account_question_label
    }
    val label = when (state) {
        SIGN_IN -> R.string.label_sign_up
        else -> R.string.label_sign_in
    }
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle()) { append(context.getString(clickableText)) }
        append(" ")
        withStyle(
            style = SpanStyle(
                color = (MaterialTheme.colorScheme).primary,
                fontWeight = FontWeight.Bold,
            ),
        ) {
            append(context.getString(label))
        }
    }

    ClickableText(
        text = annotatedText,
        style = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        onClick = { onClick() },
        modifier = modifier
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
            style = SpanStyle(
                color = (MaterialTheme.colorScheme).primary,
                fontWeight = FontWeight.Bold,
            ),
        ) {
            append(" ${stringResource(R.string.terms_text_button)} ")
        }
        pop()
        append(" ${stringResource(R.string.and_separator)} ")
        pushStringAnnotation(tag = URI_B, annotation = PRIVACY_URI)
        withStyle(
            style = SpanStyle(
                color = (MaterialTheme.colorScheme).primary,
                fontWeight = FontWeight.Bold,
            ),
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
            annotatedText.getStringAnnotations(tag = URI_A, start = offset, end = offset)
                .firstOrNull()?.let { uriHandler.openUri(it.item) }

            annotatedText.getStringAnnotations(tag = URI_B, start = offset, end = offset)
                .firstOrNull()?.let { uriHandler.openUri(it.item) }
        },
    )
}

@Preview
@Composable
private fun Preview_LoginContent() {
    PsTheme {
        PsBackground {
            AuthScreen(
                uiState = AuthState(),
                onEmailChange = {},
                onPasswordChange = {},
                onRepeatPasswordChange = {},
                onBackPressed = {},
                onSubmit = {},
                onPasswordVisibilityChange = {},
                onEventActionChange = {},
                isDialogShow = false,
            )
        }
    }
}
