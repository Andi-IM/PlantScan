package com.github.andiim.orchidscan.app.ui.screens.list

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.orchidscan.app.data.model.Plant
import com.github.andiim.orchidscan.app.ui.theme.PlantScanTheme

@Composable
fun PlantListScreen(
    onDetails: (Plant) -> Unit,
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlantListViewModel = hiltViewModel()
) {
    PlantListContent()
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