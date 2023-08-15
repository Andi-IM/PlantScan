package com.github.andiim.plantscan.app.ui.common.composables

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
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
class PlantItemTest {
  @get:Rule(order = 1) val hilt = HiltAndroidRule(this)

  @get:Rule(order = 2) val composeRule = createAndroidComposeRule<PlantScanActivity>()
  private val dummy = DataDummy.PLANTS.first()
  @Before
  fun setUp() {
    hilt.inject()
    composeRule.setContentOnActivity { PlantScanTheme { PlantItem(dummy) } }
  }

  @Test
  fun get_item_card_value() {
    composeRule.onNodeWithTag("Plant Name", useUnmergedTree = true).assert(hasText(dummy.name))
    composeRule
        .onNodeWithTag("Plant Known Names", useUnmergedTree = true)
        .assert(hasText(dummy.commonName!!.joinToString(", ")))
  }
}
