package com.github.andiim.plantscan.feature.login

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.andiim.plantscan.core.designsystem.theme.PlantScanTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

  @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

  @Mock private val viewModel = mock(LoginViewModel::class.java)
  private val openWeb: (String) -> Unit = {}
  private val openAndPopUp: (String, String) -> Unit = { _, _ -> }
  private val state = MutableStateFlow(LoginState())

  @Before
  fun setUp() {
    `when`(viewModel.state).thenReturn(state)
    composeRule.setContent {
      PlantScanTheme {
        Surface {
          LoginScreen(
              viewModel = viewModel,
              openWeb = openWeb,
              openAndPopUp = openAndPopUp,
          )
        }
      }
    }
  }

  @Test
  fun should_appear_when_starting() {
    composeRule.onRoot(true).printToLog("LoginScreen Log")
    composeRule.onNodeWithTag("Login Content").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Email Field").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Password Field").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Sign In Button").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Forgot Password Button").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Terms Label").assertIsDisplayed()
  }
  @Test
  fun field_should_change_when_take_action_and_button_action() = runTest {
    val email = "email"
    val password = "password"
    composeRule.onNodeWithContentDescription("Email Field").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Email Field").performTextInput(email)
    composeRule.onNodeWithContentDescription("Password Field").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Password Field").performTextInput(password)

    composeRule.onNodeWithContentDescription("Sign In Button").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Sign In Button").performClick()

    verify(viewModel).onSignInClick(openAndPopUp)
  }

  @Test
  fun action_forgot_password_should_executed() = runTest {
    composeRule.onNodeWithContentDescription("Forgot Password Button").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Forgot Password Button").performClick()
    verify(viewModel).onForgotPasswordClick()
  }
}
