package com.github.andiim.plantscan.app.ui.screens.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.andiim.plantscan.app.data.model.Plant
import com.github.andiim.plantscan.app.ui.common.composables.BasicToolbar
import com.github.andiim.plantscan.app.ui.common.composables.PlantPagedList
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.github.andiim.plantscan.app.R.string as AppText

@Composable
fun PlantListScreen(
    toDetails: (Plant) -> Unit,
    popUpScreen: () -> Unit,
    viewModel: PlantListViewModel = hiltViewModel()
) {
    PlantListContent(
        data = viewModel.fetchedData,
        onItemClick = toDetails,
        popUpScreen = popUpScreen
    )
}

@Composable
fun PlantListContent(
    popUpScreen: () -> Unit = {},
    onItemClick: (Plant) -> Unit = {},
    data: Flow<PagingData<Plant>> = flowOf()
) {
    val plants = data.collectAsLazyPagingItems()

    BasicToolbar(
        leading = {
            IconButton(onClick = popUpScreen) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(AppText.plants_exit)
                )
            }
        },
        title = AppText.plants_label
    )
    PlantPagedList(plants = plants, onItemClick = onItemClick)
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