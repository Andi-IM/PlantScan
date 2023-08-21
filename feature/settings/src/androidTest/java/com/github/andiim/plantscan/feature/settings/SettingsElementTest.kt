package com.github.andiim.plantscan.feature.settings

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.andiim.plantscan.domain.firebase_services.AccountService
import com.github.andiim.plantscan.domain.firebase_services.LogService
import com.github.andiim.plantscan.core.designsystem.theme.PlantScanTheme
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class SettingsElementTest {

  @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()
  @Mock private val accountService = mock(com.github.andiim.plantscan.domain.firebase_services.AccountService::class.java)
  @Mock private val logService = mock(com.github.andiim.plantscan.domain.firebase_services.LogService::class.java)
  private lateinit var viewModel: SettingsViewModel
  private val openScreen: (String) -> Unit = {}
  private val restartApp: (String) -> Unit = {}

  @Test
  fun should_appear_when_starting_as_anonymous() {
    `when`(accountService.currentUser).thenReturn(flow { emit(
        com.github.andiim.plantscan.model.data.User(
            "user",
            true
        )
    ) })
    viewModel = SettingsViewModel(accountService, logService)
    composeRule.setContent {
      PlantScanTheme {
        Surface {
          SettingsElement(viewModel = viewModel, restartApp = restartApp, openScreen = openScreen)
        }
      }
    }

    composeRule.onRoot(true).printToLog("SettingsScreen Log")
    composeRule.onNodeWithTag("Settings Content").assertIsDisplayed()
    composeRule.onNodeWithTag("To Sign In Button").assertIsDisplayed()
  }

  @Test
  fun should_appear_when_starting_as_user() = runTest {
    `when`(accountService.currentUser).thenReturn(flow { emit(
        com.github.andiim.plantscan.model.data.User(
            "user",
            false
        )
    ) })
    viewModel = SettingsViewModel(accountService, logService)
    composeRule.setContent {
      PlantScanTheme {
        Surface {
          SettingsElement(viewModel = viewModel, restartApp = restartApp, openScreen = openScreen)
        }
      }
    }
    composeRule.onNodeWithTag("Settings Content").assertIsDisplayed()
    composeRule.onNodeWithTag("Sign Out Card").assertIsDisplayed()
    composeRule.onNodeWithTag("Delete Account Card").assertIsDisplayed()
  }

  @Test
  fun warning_should_appear_when_sign_out() = runTest {
    `when`(accountService.currentUser).thenReturn(flow { emit(
        com.github.andiim.plantscan.model.data.User(
            "user",
            false
        )
    ) })
    viewModel = SettingsViewModel(accountService, logService)
    composeRule.setContent {
      PlantScanTheme {
        Surface {
          SettingsElement(viewModel = viewModel, restartApp = restartApp, openScreen = openScreen)
        }
      }
    }
    composeRule.onNodeWithTag("Settings Content").assertIsDisplayed()
    composeRule.onNodeWithTag("Sign Out Card").assertIsDisplayed()
    composeRule.onNodeWithTag("Sign Out Card").performClick()
    composeRule.onNodeWithTag("Sign Out Warning Dialog").assertIsDisplayed()
  }

  @Test
  fun warning_should_appear_when_delete_account() = runTest {
    `when`(accountService.currentUser).thenReturn(flow { emit(
        com.github.andiim.plantscan.model.data.User(
            "user",
            false
        )
    ) })
    viewModel = SettingsViewModel(accountService, logService)
    composeRule.setContent {
      PlantScanTheme {
        Surface {
          SettingsElement(viewModel = viewModel, restartApp = restartApp, openScreen = openScreen)
        }
      }
    }
    composeRule.onNodeWithTag("Settings Content").assertIsDisplayed()
    composeRule.onNodeWithTag("Delete Account Card").assertIsDisplayed()
    composeRule.onNodeWithTag("Delete Account Card").performClick()
    composeRule.onNodeWithTag("Delete Account Warning Dialog").assertIsDisplayed()
  }
}
