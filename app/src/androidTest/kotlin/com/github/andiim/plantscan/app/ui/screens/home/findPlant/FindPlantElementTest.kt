package com.github.andiim.plantscan.app.ui.screens.home.findPlant

import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.github.andiim.plantscan.app.PlantScanActivity
import com.github.andiim.plantscan.app.setContentOnActivity
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class FindPlantElementTest {
  @get:Rule(order = 1) val hilt = HiltAndroidRule(this)

  @get:Rule(order = 2) val composeRule = createAndroidComposeRule<PlantScanActivity>()
  private lateinit var viewModel: FindPlantViewModel

  @Before
  fun setUp() {
    hilt.inject()
    viewModel = composeRule.activity.viewModels<FindPlantViewModel>().value
    composeRule.setContentOnActivity {
      PlantScanTheme {
        Surface {
          FindPlantElement(viewModel = viewModel, onDetails = {}, toDetect = {}, toPlantType = {})
        }
      }
    }
  }

  @Test
  fun should_appear_when_starting() {
    composeRule.onRoot(true).printToLog("SearchScreen Log")
    composeRule.onNodeWithTag("Find Plant Content").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Detect Button").assertIsDisplayed()
    composeRule.onNodeWithContentDescription("Manual Find Button").assertIsDisplayed()
    composeRule.onNodeWithTag("Search Bar").assertIsDisplayed()
  }

  @Test
  fun plant_list_should_appear_when_search_clicked() {
    composeRule.onNodeWithTag("Search Bar").assertIsDisplayed()
    composeRule.onNodeWithTag("Search Bar").performClick()
    composeRule.onNodeWithContentDescription("Plant Lists").assertIsDisplayed()
  }
}
