package com.github.andiim.plantscan.app

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.github.andiim.plantscan.app.ui.screens.list.PlantListScreen
import com.github.andiim.plantscan.app.ui.screens.list.PlantListViewModel
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PlantScanAppTest {

  @get:Rule(order = 1) val hilt = HiltAndroidRule(this)

  @get:Rule(order = 2) val composeRule = createAndroidComposeRule<PlantScanActivity>()
  private lateinit var viewModel: PlantListViewModel

  @Before
  fun setUp() {
    hilt.inject()
    composeRule.activity.setContent {
      viewModel = composeRule.activity.viewModels<PlantListViewModel>().value
      PlantScanTheme {
        PlantListScreen(
            toDetails = { _,
              ->
            },
            popUpScreen = {},
            viewModel = viewModel)
      }
    }
  }

  @Test
  fun testing() {
    composeRule.onNodeWithText("Plants").assertIsDisplayed()
  }
}
