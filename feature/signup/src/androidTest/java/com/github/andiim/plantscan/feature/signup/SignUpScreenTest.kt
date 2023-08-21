package com.github.andiim.plantscan.feature.signup

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
import org.mockito.Mockito
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class SignUpScreenTest {

  @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()
  @Mock private val viewModel = mock(SignUpViewModel::class.java)
  private val openAndPopUp: (String, String) -> Unit = { _, _ -> }
  private val state = MutableStateFlow(SignUpState())
  @Before
  fun setUp() {
    Mockito.`when`(viewModel.state).thenReturn(state)
    composeRule.setContent {
      PlantScanTheme {
        Surface { SignUpScreen(viewModel = viewModel, openAndPopUp = openAndPopUp) }
      }
    }
  }

  @Test
  fun should_appear_when_starting() {
    composeRule.onRoot(true).printToLog("SignUp Log")
    composeRule.onNodeWithTag("Sign Up Toolbar").assertIsDisplayed()
    composeRule.onNodeWithTag("SignUp Content").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Email Field").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Password Field").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Repeat Password Field").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Sign Up Button").assertIsDisplayed()
  }

  @Test
  fun field_should_change_when_take_action_and_button_action() = runTest {
    val email = "email"
    val password = "password"
    composeRule.onNodeWithContentDescription("Email Field").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Email Field").performTextInput(email)

    composeRule.onNodeWithContentDescription("Password Field").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Password Field").performTextInput(password)

    composeRule.onNodeWithContentDescription("Repeat Password Field").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Repeat Password Field").performTextInput(password)

    composeRule.onNodeWithContentDescription("Sign Up Button").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Sign Up Button").performClick()

    Mockito.verify(viewModel).onSignUpClick(openAndPopUp)
  }
}
