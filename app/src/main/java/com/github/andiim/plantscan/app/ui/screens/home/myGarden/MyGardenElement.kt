package com.github.andiim.plantscan.app.ui.screens.home.myGarden

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.andiim.plantscan.app.data.model.Plant
import com.github.andiim.plantscan.app.ui.common.composables.PlantPagedList
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun MyGardenElement(
    toDetail: (Plant) -> Unit,
    viewModel: MyGardenViewModel = hiltViewModel()
) {
    MyGardenContent(plant = viewModel.myPlant, onItemClick = toDetail)
}

@Composable
fun MyGardenContent(
    plant: Flow<PagingData<Plant>> = flowOf(),
    onItemClick: (Plant) -> Unit = {},
) {
    PlantPagedList(plants = plant.collectAsLazyPagingItems(), onItemClick = onItemClick)
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