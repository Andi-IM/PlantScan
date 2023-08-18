package com.github.andiim.plantscan.app.ui.screens.auth.signUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.ui.common.composables.BasicButton
import com.github.andiim.plantscan.app.ui.common.composables.BasicToolbar
import com.github.andiim.plantscan.app.ui.common.composables.EmailField
import com.github.andiim.plantscan.app.ui.common.composables.PasswordField
import com.github.andiim.plantscan.app.ui.common.composables.RepeatPasswordField
import com.github.andiim.plantscan.app.ui.common.extensions.basicButton
import com.github.andiim.plantscan.app.ui.common.extensions.fieldModifier
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme

@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
  val state by viewModel.state.collectAsState()
  SignUpContent(
      openAndPopUp = openAndPopUp,
      uiState = state,
      onEmailChange = viewModel::onEmailChange,
      onPasswordChange = viewModel::onPasswordChange,
      onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
      onSignUpClick = viewModel::onSignUpClick,
  )
}

@Composable
fun SignUpContent(
    openAndPopUp: (String, String) -> Unit = { _, _ -> },
    uiState: SignUpState = SignUpState(),
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onRepeatPasswordChange: (String) -> Unit = {},
    onSignUpClick: ((String, String) -> Unit) -> Unit = {}
) {
  BasicToolbar(
      modifier = Modifier.testTag("Sign Up Toolbar"),
      title = R.string.label_create_account
  )

  Column(
      modifier =
          Modifier.fillMaxWidth()
              .fillMaxHeight()
              .testTag("SignUp Content")
              .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        EmailField(
            uiState.email,
            onEmailChange,
            Modifier.fieldModifier().semantics(true) { contentDescription = "Email Field" })
        PasswordField(
            uiState.password,
            onPasswordChange,
            Modifier.fieldModifier().semantics(true) { contentDescription = "Password Field" })
        RepeatPasswordField(
            uiState.repeatPassword,
            onRepeatPasswordChange,
            Modifier.fieldModifier().semantics(true) {
              contentDescription = "Repeat Password Field"
            })

        BasicButton(
            R.string.label_create_account,
            Modifier.basicButton().semantics(true) { contentDescription = "Sign Up Button" }) {
              onSignUpClick(openAndPopUp)
            }
      }
}

@Preview
@Composable
private fun Preview_SignUpScreen() {
  PlantScanTheme { Surface { SignUpContent() } }
}
