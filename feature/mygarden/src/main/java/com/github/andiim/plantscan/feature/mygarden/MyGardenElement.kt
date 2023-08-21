package com.github.andiim.plantscan.feature.mygarden

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.plantscan.core.designsystem.theme.PlantScanTheme

@Composable
fun MyGardenElement(toDetail: (String) -> Unit, viewModel: MyGardenViewModel = hiltViewModel()) {
  val plant = viewModel.myPlant.collectAsState()
  MyGardenContent(plant = plant.value, onItemClick = toDetail)
}

@Composable
fun MyGardenContent(
    plant: List<com.github.andiim.plantscan.model.data.Plant> = listOf(),
    onItemClick: (String) -> Unit = {},
) {
  // PlantPagedList(plants = plant.collectAsLazyPagingItems(), onItemClick = onItemClick)
}

@Preview
@Composable
private fun Preview_MyGardenContent() {
  PlantScanTheme { Surface { MyGardenContent() } }
}
