package com.github.andiim.plantscan.feature.mygarden

import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.github.andiim.plantscan.app.PlantScanActivity
import com.github.andiim.plantscan.app.setContentOnActivity
import com.github.andiim.plantscan.core.designsystem.theme.PlantScanTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MyGardenElementTest {
  @get:Rule(order = 1) val hilt = HiltAndroidRule(this)

  @get:Rule(order = 2) val composeRule = createAndroidComposeRule<PlantScanActivity>()
  private lateinit var viewModel: MyGardenViewModel

  @Before
  fun setUp() {
    hilt.inject()
    viewModel = composeRule.activity.viewModels<MyGardenViewModel>().value
    composeRule.setContentOnActivity {
      PlantScanTheme { Surface { MyGardenElement(toDetail = {}, viewModel = viewModel) } }
    }
  }

  @Test
  fun config() {
    composeRule.onRoot(true).printToLog("Detail Log")
    // TODO : IMPLEMENT DATA FIRST TO MY GARDEN SCREEN!

    /* Test Case
    1. Should Show Item List That Saved To Garden
    2. Should Show Image for Each Item That Saved To Garden
    3. Should Show Name (Common Name) n/b: if multiple data, take first thing.
    4. Should Show Species Name
     */
  }
}
