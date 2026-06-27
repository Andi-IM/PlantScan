package com.github.andiim.plantscan.app.ui

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import com.github.andiim.plantscan.core.testing.util.TestNetworkMonitor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Test [PsAppState].
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class PsAppStateTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val networkMonitor = TestNetworkMonitor()
    private lateinit var state: PsAppState

    @Test
    fun psAppState_currentDestination() = runTest {
        var currentDestination: String? = null
        composeTestRule.setContent {
            val navController = rememberTestNavController()
            state = remember(navController) {
                PsAppState(
                    navController = navController,
                    networkMonitor = networkMonitor,
                    windowSizeClass = getCompactWindowClass(),
                    coroutineScope = backgroundScope,
                )
            }
            currentDestination = state.currentDestination?.route
            LaunchedEffect(Unit) {
                navController.setCurrentDestination("b")
            }
        }

        assertEquals("b", currentDestination)
    }

    @Test
    fun psAppSate_destinations() = runTest {
        composeTestRule.setContent {
            state = rememberPsAppState(
                windowSizeClass = getCompactWindowClass(),
                networkMonitor = networkMonitor,
            )
        }
        assertEquals(3, state.topLevelDestinations.size)
        assertTrue(state.topLevelDestinations[0].name.contains("find_plant", true))
        assertTrue(state.topLevelDestinations[1].name.contains("history", true))
        assertTrue(state.topLevelDestinations[2].name.contains("settings", true))
    }

    @Test
    fun psAppState_showBottomBar_compact() = runTest {
        composeTestRule.setContent {
            state = PsAppState(
                navController = NavHostController(LocalContext.current),
                networkMonitor = networkMonitor,
                windowSizeClass = getCompactWindowClass(),
                coroutineScope = backgroundScope,
            )
        }

        assertTrue(state.shouldShowBottomBar)
        assertFalse(state.shouldShowNavRail)
    }

    @Test
    fun psAppState_showNavRail_medium() = runTest {
        composeTestRule.setContent {
            state = PsAppState(
                navController = NavHostController(LocalContext.current),
                networkMonitor = networkMonitor,
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(800.dp, 800.dp)),
                coroutineScope = backgroundScope,
            )
        }

        assertTrue(state.shouldShowNavRail)
        assertFalse(state.shouldShowBottomBar)
    }

    @Test
    fun psAppState_showNavRail_large() = runTest {
        composeTestRule.setContent {
            state = PsAppState(
                navController = NavHostController(LocalContext.current),
                networkMonitor = networkMonitor,
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(900.dp, 1200.dp)),
                coroutineScope = backgroundScope,
            )
        }

        assertTrue(state.shouldShowNavRail)
        assertFalse(state.shouldShowBottomBar)
    }

    @Test
    fun stateIsOfflineWhenNetworkMonitorIsOffline() = runTest(UnconfinedTestDispatcher()) {
        composeTestRule.setContent {
            state = PsAppState(
                navController = NavHostController(LocalContext.current),
                networkMonitor = networkMonitor,
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(900.dp, 1200.dp)),
                coroutineScope = backgroundScope,
            )
        }

        backgroundScope.launch { state.isOffline.collect() }
        networkMonitor.setConnected(false)
        assertEquals(
            true,
            state.isOffline.value,
        )
    }

    private fun getCompactWindowClass() = WindowSizeClass.calculateFromSize(DpSize(500.dp, 300.dp))
}

@Composable
private fun rememberTestNavController(): TestNavHostController {
    val context = LocalContext.current
    return remember {
        TestNavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            graph = createGraph(startDestination = "a") {
                composable("a") { }
                composable("b") { }
                composable("c") { }
            }
        }
    }
}
