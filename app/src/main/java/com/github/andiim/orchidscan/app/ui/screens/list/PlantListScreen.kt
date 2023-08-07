package com.github.andiim.orchidscan.app.ui.screens.list

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.andiim.orchidscan.app.ui.theme.PlantScanTheme

@Composable
fun PlantListScreen() {
    Box {
        Text("Hello World!")
        PlantListContent()
    }
}

@Composable
fun PlantListContent() {

}

@Preview
@Composable
private fun Preview_PlantListContent() {
    PlantScanTheme {
        Surface {
            PlantListContent()
        }
    }
}