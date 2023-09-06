package com.github.andiim.plantscan.app.ui.screens.home.history

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme

@Composable
fun MyGardenElement(toDetail: (String) -> Unit, viewModel: MyGardenViewModel = hiltViewModel()) {
    MyGardenContent(onItemClick = toDetail)
}

@Composable
fun MyGardenContent(
    plant: List<Plant> = listOf(),
    onItemClick: (String) -> Unit = {},
) {
    // PlantPagedList(plants = plant.collectAsLazyPagingItems(), onItemClick = onItemClick)
}

@Preview
@Composable
private fun Preview_MyGardenContent() {
    PlantScanTheme { Surface { MyGardenContent() } }
}
