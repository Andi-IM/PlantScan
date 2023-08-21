package com.github.andiim.plantscan.feature.list

import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.github.andiim.plantscan.app.PlantScanActivity
import com.github.andiim.plantscan.app.setContentOnActivity
import com.github.andiim.plantscan.app.ui.screens.plants.list.PlantListScreen
import com.github.andiim.plantscan.app.ui.screens.plants.list.PlantListViewModel
import com.github.andiim.plantscan.core.designsystem.theme.PlantScanTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PlantListScreenTest {
  @get:Rule(order = 1) val hilt = HiltAndroidRule(this)

  @get:Rule(order = 2) val composeRule = createAndroidComposeRule<PlantScanActivity>()
  private lateinit var viewModel: PlantListViewModel

  @Before
  fun setUp() {
    hilt.inject()
    viewModel = composeRule.activity.viewModels<PlantListViewModel>().value
    composeRule.setContentOnActivity {
      PlantScanTheme {
        Surface { PlantListScreen(toDetails = {}, popUpScreen = {}, viewModel = viewModel) }
      }
    }
  }

  @Test
  fun config() {
    composeRule.onRoot(true).printToLog("Detail Log")
  }
}
