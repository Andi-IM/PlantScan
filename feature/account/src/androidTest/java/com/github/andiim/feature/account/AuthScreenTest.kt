package com.github.andiim.feature.account

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.github.andiim.plantscan.feature.account.AuthScreen
import com.github.andiim.plantscan.feature.account.model.AuthState
import org.junit.Rule
import org.junit.Test

/**
 * Ui tests for [AuthScreen].
 */
class AuthScreenTest {
    @get:Rule
    val composableTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun when_typing_email_address_should_change_email_state() {
        composableTestRule.setContent {
            var uiState by remember { mutableStateOf(AuthState()) }
            var visiblePassword by remember { mutableStateOf(false) }
            AuthScreen(
                uiState = uiState,
                isDialogShow = false,
                onSubmit = {},
                onBackPressed = {},
                onEmailChange = { uiState = uiState.copy(email = it) },
                onPasswordChange = { uiState = uiState.copy(password = it) },
                onPasswordVisibilityChange = {
                    visiblePassword = !visiblePassword
                    uiState = uiState.copy(isVisiblePassword = visiblePassword)
                },
                onRepeatPasswordChange = {},
                onEventActionChange = {},
            )
        }
        composableTestRule.onNodeWithTag("emailTextField").performClick()
        composableTestRule.onNodeWithTag("emailTextField").assertIsFocused()
        composableTestRule.onNodeWithTag("emailTextField").performTextInput("email@gmail.com")
        composableTestRule.onNodeWithText("email@gmail.com").assertIsDisplayed()
    }

    @Test
    fun when_typing_password_address_should_change_email_state() {
        composableTestRule.setContent {
            var uiState by remember { mutableStateOf(AuthState()) }
            var visiblePassword by remember { mutableStateOf(false) }
            AuthScreen(
                uiState = uiState,
                isDialogShow = false,
                onSubmit = {},
                onBackPressed = {},
                onEmailChange = {},
                onPasswordChange = { uiState = uiState.copy(password = it) },
                onPasswordVisibilityChange = {
                    visiblePassword = !visiblePassword
                    uiState = uiState.copy(isVisiblePassword = visiblePassword)
                },
                onRepeatPasswordChange = {},
                onEventActionChange = {},
            )
        }
        composableTestRule.onNodeWithTag("passwordTextField").performClick()
        composableTestRule.onNodeWithTag("passwordTextField").assertIsFocused()
        composableTestRule.onNodeWithContentDescription("passwordVisibility").performClick()
        composableTestRule.onNodeWithContentDescription("Visible").assertExists()
        composableTestRule.onNodeWithTag("passwordTextField").performClick()
        composableTestRule.onNodeWithTag("passwordTextField").performTextInput("123456")
        composableTestRule.onNodeWithText("123456").assertIsDisplayed()
    }

    @Test
    fun when_typing_repeat_password_address_should_change_email_state() {
        composableTestRule.setContent {
            var uiState by remember { mutableStateOf(AuthState()) }
            var visiblePassword by remember { mutableStateOf(false) }
            AuthScreen(
                uiState = uiState,
                isDialogShow = false,
                onSubmit = {},
                onBackPressed = {},
                onEmailChange = {},
                onPasswordChange = {},
                onPasswordVisibilityChange = {
                    visiblePassword = !visiblePassword
                    uiState = uiState.copy(isVisiblePassword = visiblePassword)
                },
                onRepeatPasswordChange = { uiState = uiState.copy(password = it) },
                onEventActionChange = { uiState = uiState.copy(eventAction = it) },
            )
        }
        composableTestRule.onNodeWithTag("changeMode").performClick()
        composableTestRule.onNodeWithTag("repeatPasswordTextField").assertExists()
        composableTestRule.onNodeWithTag("repeatPasswordTextField").performClick()
        composableTestRule.onNodeWithTag("repeatPasswordTextField").assertIsFocused()
        composableTestRule.onNodeWithContentDescription("repeatPasswordVisibility").performClick()
        composableTestRule.onNodeWithTag("repeatPasswordTextField").performClick()
        composableTestRule.onNodeWithTag("repeatPasswordTextField").performTextInput("123456")
        composableTestRule.onNodeWithText("123456").assertIsDisplayed()
    }
}
