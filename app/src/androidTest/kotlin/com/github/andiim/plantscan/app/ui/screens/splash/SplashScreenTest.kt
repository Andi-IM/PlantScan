package com.github.andiim.plantscan.app.ui.screens.splash

import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.andiim.plantscan.app.PlantScanActivity
import com.github.andiim.plantscan.app.R.string as AppText
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.AccountServiceManipulator
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.AccountServiceManipulatorImpl
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.FakeAccountServiceImpl
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.FakeConfigurationServiceImpl
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.FakeLogServiceImpl
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.AccountService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.ConfigurationService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.onNodeWithStringId
import com.github.andiim.plantscan.app.setContentOnActivity
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SplashScreenTest {
  @get:Rule(order = 1) val hilt = HiltAndroidRule(this)

  @get:Rule(order = 2) val composeRule = createAndroidComposeRule<PlantScanActivity>()
  private lateinit var viewModel: SplashViewModel
  private lateinit var account: AccountService
  private lateinit var serviceManipulator: AccountServiceManipulator
  private lateinit var config: ConfigurationService
  private lateinit var log: LogService

  @Before
  fun setUp() {
    hilt.inject()

    serviceManipulator = AccountServiceManipulatorImpl()
    account = FakeAccountServiceImpl(serviceManipulator)
    config = FakeConfigurationServiceImpl()
    log = FakeLogServiceImpl()

    viewModel =
        SplashViewModel(accountService = account, configurationService = config, logService = log)
    composeRule.setContentOnActivity {
      PlantScanTheme { Surface { SplashScreen(openAndPopUp = { _, _ -> }, viewModel = viewModel) } }
    }
  }

  @Test
  fun check_loading_showed() {
    composeRule.onNodeWithTag("Progress").assertIsDisplayed()
  }

  @Test
  fun check_error_showed_when_something_error() {
    viewModel.showError.value = true
    composeRule.onNodeWithStringId(AppText.generic_error, true).assertIsDisplayed()
    composeRule.onNodeWithTag("Reload Button", true).assertIsDisplayed()
  }

  @Test
  fun reloading_success_when_something_error() {
    viewModel.showError.value = true
    composeRule.onNodeWithStringId(AppText.generic_error, true).assertIsDisplayed()
    composeRule.onNodeWithTag("Reload Button", true).assertIsDisplayed()
    composeRule.onNodeWithTag("Reload Button").performClick()
    viewModel.showError.value = false
    composeRule.onNodeWithTag("Progress").assertIsDisplayed()
  }
}
