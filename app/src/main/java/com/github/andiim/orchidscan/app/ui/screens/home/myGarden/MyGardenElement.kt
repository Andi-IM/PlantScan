package com.github.andiim.orchidscan.app.ui.screens.home.myGarden

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.orchidscan.app.ui.theme.PlantScanTheme

@Composable
fun MyGardenElement(viewModel: MyGardenViewModel = hiltViewModel()) {
    Box { Text("Hello World!") }
}

@Composable
fun MyGardenContent() {

}

@Preview
@Composable
private fun Preview_MyGardenContent() {
    PlantScanTheme {
        Surface {
            MyGardenContent()
        }
    }
}