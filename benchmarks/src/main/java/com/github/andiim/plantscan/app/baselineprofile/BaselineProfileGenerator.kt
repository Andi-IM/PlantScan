package com.github.andiim.plantscan.app.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import com.github.andiim.plantscan.app.PACKAGE_NAME
import org.junit.Rule
import org.junit.Test

/**
 * Generates a baseline profile which can be copied to `app/src/main/baseline-prof.txt`.
 */
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(PACKAGE_NAME) {
        // This block defines the app's critical user journey. Here we are interested in
        // optimizing for app startup. But you can also navigate and scroll
        // through your most important UI.
        // TODO: CREATE USER JOURNEY FOR BENCHMARK.
    }
}
