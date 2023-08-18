package com.github.andiim.plantscan.app.ui.screens.home.myGarden

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme

@Composable
fun MyGardenElement(toDetail: (Plant) -> Unit, viewModel: MyGardenViewModel = hiltViewModel()) {
  val plant = viewModel.myPlant.collectAsState()
  MyGardenContent(plant = plant.value, onItemClick = toDetail)
}

@Composable
fun MyGardenContent(
    plant: List<Plant> = listOf(),
    onItemClick: (Plant) -> Unit = {},
) {
  // PlantPagedList(plants = plant.collectAsLazyPagingItems(), onItemClick = onItemClick)
}

@Preview
@Composable
private fun Preview_MyGardenContent() {
  PlantScanTheme { Surface { MyGardenContent() } }
}
