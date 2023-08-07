package com.github.andiim.orchidscan.app.ui.screens.home.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.andiim.orchidscan.app.ui.theme.PlantScanTheme

@Composable
fun SettingsElement() {
    Box { Text("Hello World!") }
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