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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
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
import com.github.andiim.plantscan.app.R.string as AppString
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
      onForgotPasswordClick = viewModel::onForgotPasswordClick)
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
  Box(modifier = Modifier.padding(24.dp).testTag("Login Content")) {
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
          EmailField(
              email,
              onEmailChange,
              Modifier.fieldModifier().semantics(true) { contentDescription = "Email Field" })
          PasswordField(
              password,
              onPasswordChange,
              Modifier.fieldModifier().semantics(true) { contentDescription = "Password Field" })

          BasicButton(
              R.string.label_sign_in,
              Modifier.basicButton().semantics(true) { contentDescription = "Sign In Button" },
              action = { onSignInClick(openAndPopUp) })

          BasicTextButton(
              R.string.hint_forgot_password,
              Modifier.textButton().semantics(true) {
                contentDescription = "Forgot Password Button"
              },
              action = onForgotPasswordClick)
        }
    TermsAndPrivacyStatementText(
        annotatedText = generateAnnotatedText(),
        openWeb = openWeb,
        modifier =
            Modifier.align(Alignment.BottomCenter).semantics(true) {
              contentDescription = "Terms Label"
            })
  }
}

@Composable
private fun generateAnnotatedText(): AnnotatedString {
  return buildAnnotatedString {
    withStyle(style = SpanStyle()) { append(stringResource(AppString.terms_label)) }
    pushStringAnnotation(tag = "URL_A", annotation = "android.com")
    withStyle(
        style =
            SpanStyle(color = (MaterialTheme.colorScheme).primary, fontWeight = FontWeight.Bold)) {
          append(" ${stringResource(AppString.terms_text_button)} ")
        }
    append(" ${stringResource(AppString.and_separator)} ")
    pushStringAnnotation(tag = "URL_B", annotation = "developer.android.com")
    withStyle(
        style =
            SpanStyle(color = (MaterialTheme.colorScheme).primary, fontWeight = FontWeight.Bold)) {
          append(" ${stringResource(AppString.privacy_policy_text_button)} ")
        }
    pop()
  }
}

@Composable
private fun TermsAndPrivacyStatementText(
    annotatedText: AnnotatedString,
    modifier: Modifier = Modifier,
    openWeb: (String) -> Unit,
) {
  ClickableText(
      text = annotatedText,
      modifier = modifier,
      style = TextStyle.Default.copy(textAlign = TextAlign.Center, fontSize = 13.sp)) { offset ->
        annotatedText.getStringAnnotations(offset, offset).firstOrNull()?.tag?.let { tag ->
          if (tag == "URL_A") {
            openWeb(annotatedText.text)
          }
          if (tag == "URL_B") {
            openWeb(annotatedText.text)
          }
        }
      }
}

@Preview
@Composable
private fun Preview_LoginContent() {
  PlantScanTheme { Surface { LoginContent(openAndPopUp = { _, _ -> }) } }
}
