package com.github.andiim.plantscan.app

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.github.andiim.plantscan.app.utils.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class PlantScanAppTest {

  @get:Rule(order = 1) val hilt = HiltAndroidRule(this)
  @get:Rule(order = 2) val activity = ActivityScenarioRule(PlantScanActivity::class.java)

  @Before
  fun setUp() {
    hilt.inject()
    IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
  }

  @After
  fun tearDown() {
    IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
  }

  @Test
  fun navHost_verifyStartDestination() {
    Intents.init()
  }
}
