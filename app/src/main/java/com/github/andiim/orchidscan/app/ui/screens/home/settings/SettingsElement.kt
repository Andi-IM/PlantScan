package com.github.andiim.orchidscan.app.ui.screens.home.settings

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.orchidscan.app.ui.theme.PlantScanTheme

@Composable
fun SettingsElement(
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    SettingsContent()
}

@Composable
fun SettingsContent() {

}

@Preview
@Composable
private fun Preview_SettingsContent() {
    PlantScanTheme {
        Surface {
            SettingsContent()
        }
    }
}