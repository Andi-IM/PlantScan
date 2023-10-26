package com.github.andiim.plantscan.app.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.ui.navigation.Host
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import com.github.andiim.plantscan.app.core.utils.NetworkMonitor
import com.github.andiim.plantscan.app.core.utils.snackbar.SnackbarManager
import com.github.andiim.plantscan.app.core.utils.snackbar.SnackbarMessage

@Composable
fun MainApp(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    state: AppState = rememberAppState(
        networkMonitor = networkMonitor,
        windowSizeClass = windowSizeClass
    )
) {
    PlantScanTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            val isOffline by state.isOffline.collectAsStateWithLifecycle()
            val context = LocalContext.current
            LaunchedEffect(isOffline) {
                if (isOffline) {
                    SnackbarManager.showMessage(
                        message = context.resources.getString(R.string.not_connected),
                        duration = SnackbarDuration.Indefinite,
                        isError = true
                    )
                }
            }

            Scaffold(
                modifier = modifier.fillMaxSize(),
                contentColor = MaterialTheme.colorScheme.onBackground,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                snackbarHost = { AppSnackbarHost(state = state.snackbarHostState) },
                bottomBar = {
                    if (state.shouldShowBottomBar) {

                    }
                },
            ) { paddingValues ->
                Host(
                    state = state,
                    modifier = modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun AppNavRail() {

}

@Composable
fun AppSnackbarHost(state: SnackbarHostState, modifier: Modifier = Modifier): Unit {
    SnackbarHost(hostState = state) { snackbarData ->
        val isError = (snackbarData.visuals as? SnackbarMessage)?.isError ?: false
        val buttonColor = ButtonDefaults.getButtonColor(isError)

        Snackbar(
            modifier = modifier.padding(12.dp),
            action = {
                if (isError) {
                    TextButton(
                        onClick = { snackbarData.performAction() },
                        colors = buttonColor,
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(snackbarData.visuals.actionLabel ?: "")
                    }
                }
            }
        ) {
            Text(snackbarData.visuals.message)
        }
    }
}

@Composable
fun ButtonDefaults.getButtonColor(isError: Boolean): ButtonColors {
    return if (isError) {
        this.textButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.error
        )
    } else this.textButtonColors(contentColor = MaterialTheme.colorScheme.inversePrimary)
}


