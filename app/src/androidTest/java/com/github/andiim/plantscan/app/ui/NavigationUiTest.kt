package com.github.andiim.plantscan.app.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.github.andiim.plantscan.app.utils.BOTTOM_BAR_TAG
import com.github.andiim.plantscan.app.utils.NAV_RAIL_TAG
import com.github.andiim.plantscan.core.data.util.NetworkMonitor
import com.github.andiim.plantscan.core.rules.GrantPostNotificationsPermissionRule
import com.github.andiim.plantscan.uitesthiltmanifest.HiltComponentActivity
import com.google.accompanist.testharness.TestHarness
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import javax.inject.Inject

/**
 * Tests that the navigation UI is rendered correctly on different screen sizes.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@HiltAndroidTest
class NavigationUiTest {
    /**
     * Manages the components' state and is used to perform injection on your test.
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Create a temporary folder used to create a Data Store file. This guarantees that
     * the file is removed in between each test, preventing a crash.
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
     * Use a test activity to set the content on.
     */
    @get:Rule(order = 3)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun compactWidth_compactHeight_showsNavigationBar() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(400.dp, 400.dp)) {
                BoxWithConstraints {
                    MainApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(
                                maxWidth,
                                maxHeight,
                            ),
                        ),
                        networkMonitor = networkMonitor,
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag(BOTTOM_BAR_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(NAV_RAIL_TAG).assertDoesNotExist()
    }

    @Test
    fun expandedWidth_compactHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(900.dp, 400.dp)) {
                BoxWithConstraints {
                    MainApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(
                                maxWidth,
                                maxHeight,
                            ),
                        ),
                        networkMonitor = networkMonitor,
                    )
                }
            }
        }
        composeTestRule.onNodeWithTag(NAV_RAIL_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(BOTTOM_BAR_TAG).assertDoesNotExist()
    }

    @Test
    fun compactWidth_mediumHeight_showsNavigationBar() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(400.dp, 500.dp)) {
                BoxWithConstraints {
                    MainApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(
                                maxWidth,
                                maxHeight,
                            ),
                        ),
                        networkMonitor = networkMonitor,
                    )
                }
            }
        }
        composeTestRule.onNodeWithTag(BOTTOM_BAR_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(NAV_RAIL_TAG).assertDoesNotExist()
    }

    @Test
    fun mediumWidth_mediumHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(610.dp, 500.dp)) {
                BoxWithConstraints {
                    MainApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(
                                maxWidth,
                                maxHeight,
                            ),
                        ),
                        networkMonitor = networkMonitor,
                    )
                }
            }
        }
        composeTestRule.onNodeWithTag(NAV_RAIL_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(BOTTOM_BAR_TAG).assertDoesNotExist()
    }

    @Test
    fun expandedWidth_mediumHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(900.dp, 500.dp)) {
                BoxWithConstraints {
                    MainApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(
                                maxWidth,
                                maxHeight,
                            ),
                        ),
                        networkMonitor = networkMonitor,
                    )
                }
            }
        }
        composeTestRule.onNodeWithTag(NAV_RAIL_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(BOTTOM_BAR_TAG).assertDoesNotExist()
    }

    @Test
    fun compactWidth_expandedHeight_showsNavigationBar() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(400.dp, 1000.dp)) {
                BoxWithConstraints {
                    MainApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(
                                maxWidth,
                                maxHeight,
                            ),
                        ),
                        networkMonitor = networkMonitor,
                    )
                }
            }
        }
        composeTestRule.onNodeWithTag(BOTTOM_BAR_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(NAV_RAIL_TAG).assertDoesNotExist()
    }

    @Test
    fun expandedWidth_expandedHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(900.dp, 1000.dp)) {
                BoxWithConstraints {
                    MainApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(
                                maxWidth,
                                maxHeight,
                            ),
                        ),
                        networkMonitor = networkMonitor,
                    )
                }
            }
        }
        composeTestRule.onNodeWithTag(NAV_RAIL_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(BOTTOM_BAR_TAG).assertDoesNotExist()
    }
}
