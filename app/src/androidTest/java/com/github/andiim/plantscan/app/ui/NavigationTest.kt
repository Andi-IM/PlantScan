package com.github.andiim.plantscan.app.ui

import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.github.andiim.plantscan.core.rules.GrantPostNotificationsPermissionRule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.properties.ReadOnlyProperty
import com.github.andiim.plantscan.feature.findplant.R as findPlantR

/**
 * Tests all the navigation that are handled by the navigation library.
 */
@HiltAndroidTest
class NavigationTest {
    /**
     * Manages the components' state and is used to perform injection on your test.
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Create a temporary folder used to create a Data Store file. This guarantees that
     * the file is removed in between test, preventing a crash.
     */
    @BindValue
    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    /**
     * Grant [android.Manifest.permission.POST_NOTIFICATIONS] permission.
     */
    @get:Rule(order = 2)
    val postNotificationsPermission = GrantPostNotificationsPermissionRule()

    /**
     * Use the primary activity to initialize the app normally.
     */
    @get:Rule(order = 3)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // TODO INSERT NEEDS REPO HERE /

    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val findPlant by composeTestRule.stringResource(findPlantR.string.find_plant)

    @Before
    fun setup() = hiltRule.inject()

    @Test
    fun firstScreen_isFindPlant() {
        composeTestRule.apply {
            onNodeWithText(findPlant).assertIsSelected()
        }
    }
}
