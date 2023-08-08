package com.github.andiim.plantscan.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarManager
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarMessage.Companion.toMessage
import com.github.andiim.plantscan.app.ui.navigation.Direction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class PlantScanAppState(
    val snackbarHostState: SnackbarHostState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val context: Context,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(context.resources)
                snackbarHostState.showSnackbar(text)
            }
        }
    }

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String, singleTopLaunch: Boolean = true) {
        if (route == Direction.Detect.route) {
            toDetectActivity()
        } else {
            navController.navigate(route) { launchSingleTop = singleTopLaunch }
        }
    }

    private fun toDetectActivity() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("plantscan://detection")
            `package` = context.packageName
        }
        context.startActivity(intent)
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                // saveState = true
                inclusive = true
            }
            launchSingleTop = true
            // restoreState = true
        }
    }
}