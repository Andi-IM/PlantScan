package com.github.andiim.plantscan.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PlantScanActivity : ComponentActivity() {
  private lateinit var state: PlantScanAppState
  private lateinit var navController: NavHostController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      navController = rememberNavController()
      state = rememberAppState(navController = navController)
      PlantScanApp(appState = state)
    }
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    Timber.d("ON NEW INTENT CALLED!!!")
    navController.handleDeepLink(intent)
  }
}
