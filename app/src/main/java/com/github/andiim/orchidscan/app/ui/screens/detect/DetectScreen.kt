package com.github.andiim.orchidscan.app.ui.screens.detect

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.andiim.orchidscan.app.ui.theme.PlantScanTheme

@Composable
fun DetectScreen() {
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