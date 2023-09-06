package com.github.andiim.plantscan.app.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.ui.common.composables.BasicButton
import com.github.andiim.plantscan.app.ui.common.extensions.basicButton
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import kotlinx.coroutines.delay

private const val SPLASH_TIMEOUT = 1000L

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    openScreen: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    SplashContent(
        modifier = modifier,
        openScreen = openScreen,
        onAppStart = viewModel::onAppStart,
        isError = viewModel.showError.value
    )
}

@Composable
fun SplashContent(
    modifier: Modifier = Modifier,
    openScreen: () -> Unit = { },
    onAppStart: (() -> Unit) -> Unit = {},
    isError: Boolean = false
) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background)
            .semantics(false) {
                contentDescription = "Splash Screen Content"
            }
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        if (isError) {
            Text(text = stringResource(R.string.generic_error))

            BasicButton(
                text = R.string.try_again,
                Modifier
                    .basicButton()
                    .testTag("Reload Button"),
                action = { onAppStart(openScreen) })
        } else {
            CircularProgressIndicator(
                modifier = Modifier.testTag("Progress"),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }

    LaunchedEffect(true) {
        delay(SPLASH_TIMEOUT)
        onAppStart(openScreen)
    }
}

@Preview
@Composable
private fun Preview_PlantListContent() {
    PlantScanTheme { Surface { SplashContent() } }
}
