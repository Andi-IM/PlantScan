package com.github.andiim.orchidscan.app.ui.screens.detail

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.andiim.orchidscan.app.ui.theme.PlantScanTheme

@Composable
fun DetailScreen() {
    DetailContent()
}

@Composable
fun DetailContent() {

}

@Preview
@Composable
private fun Preview_DetailContent() {
    PlantScanTheme {
        Surface {
            DetailContent()
        }
    }
}