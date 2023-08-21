package com.github.andiim.plantscan.feature.web

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * UI tests for [WebScreen] composable
 */
@RunWith(AndroidJUnit4::class)
class WebScreenTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.github.andiim.plantscan.feature.web.test", appContext.packageName)
    }
}