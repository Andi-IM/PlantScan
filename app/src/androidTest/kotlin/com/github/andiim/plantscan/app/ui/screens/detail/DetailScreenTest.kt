package com.github.andiim.plantscan.app.ui.screens.detail

import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.github.andiim.plantscan.app.PlantScanActivity
import com.github.andiim.plantscan.app.setContentOnActivity
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import com.github.andiim.plantscan.app.utils.DataDummy
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DetailScreenTest {
  @get:Rule(order = 1) val hilt = HiltAndroidRule(this)

  @get:Rule(order = 2) val composeRule = createAndroidComposeRule<PlantScanActivity>()
  private lateinit var viewModel: DetailViewModel
  private val popupScreen: () -> Unit = {}
  private val id = DataDummy.PLANTS.first().id

  @Before
  fun setUp() {
    hilt.inject()
    viewModel = composeRule.activity.viewModels<DetailViewModel>().value
    composeRule.setContentOnActivity {
      PlantScanTheme {
        Surface { DetailScreen(id = id, popUpScreen = popupScreen, viewModel = viewModel) }
      }
    }
  }

  @Test
  fun config() {
    composeRule.onRoot(true).printToLog("Detail Log")
    // TODO : IMPLEMENT DATA FIRST TO DETAIL SCREEN!

    /* Test Case
    1. Should Show Image
    2. Should Show Name (Common Name) n/b: if multiple data, take first thing.
    3. Should Show Species Name
    4. Show Taxonomy in one card
     */

  }
}
