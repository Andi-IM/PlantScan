package com.github.andiim.orchidscan.app.ui.screens.home.findPlant

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.orchidscan.app.data.model.Plant
import com.github.andiim.orchidscan.app.ui.theme.PlantScanTheme

@Composable
fun FindPlantElement(
    modifier: Modifier = Modifier,
    onDetails: (Plant) -> Unit,
    toDetect: () -> Unit,
    toPlantType: () -> Unit,
    viewModel: FindPlantViewModel = hiltViewModel()
) {
    FindPlantContent()
}

@Composable
fun FindPlantContent() {

}

@Preview
@Composable
private fun Preview_FindPlantContent() {
    PlantScanTheme {
        Surface {
            FindPlantContent()
        }
    }
}