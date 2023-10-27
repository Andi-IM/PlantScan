package com.github.andiim.plantscan.app.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.core.utils.snackbar.SnackbarManager
import com.github.andiim.plantscan.app.core.utils.snackbar.SnackbarMessage
import com.github.andiim.plantscan.app.navigation.Host
import com.github.andiim.plantscan.core.data.util.NetworkMonitor
import com.github.andiim.plantscan.core.designsystem.component.PlantScanBackground

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainApp(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    modifier: Modifier = Modifier,
    state: AppState = rememberAppState(
        networkMonitor = networkMonitor,
        windowSizeClass = windowSizeClass
    )
) {
    PlantScanBackground {
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
            modifier = modifier
                .semantics {
                    testTagsAsResourceId = true
                },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { AppSnackbarHost(state = state.snackbarHostState) }
        ) { paddingValues ->
            Host(
                state = state,
                modifier = modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun AppSnackbarHost(state: SnackbarHostState, modifier: Modifier = Modifier) {
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
