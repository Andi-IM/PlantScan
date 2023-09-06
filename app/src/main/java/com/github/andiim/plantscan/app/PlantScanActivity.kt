package com.github.andiim.plantscan.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.andiim.plantscan.app.core.analytics.AnalyticsHelper
import com.github.andiim.plantscan.app.core.analytics.LocalAnalyticsHelper
import com.github.andiim.plantscan.app.core.data.util.NetworkMonitor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlantScanActivity : AppCompatActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private lateinit var state: PlantScanAppState
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            state = rememberAppState(navController = navController)

            CompositionLocalProvider(LocalAnalyticsHelper provides analyticsHelper) {
                PlantScanApp(appState = state)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}
