package com.github.andiim.plantscan.app.ui.screens.detect

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme

@Composable
fun DetectScreen(
    popUpScreen: () -> Unit,
    viewModel: DetectViewModel = hiltViewModel()
) {
    DetectContent()
}

@Composable
fun DetectContent() {

}

@Preview
@Composable
private fun Preview_DetectContent() {
    PlantScanTheme {
        Surface {
            DetectContent()
        }
    }
}