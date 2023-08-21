package com.github.andiim.plantscan.feature.list

import androidx.compose.foundation.layout.Column
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
import com.github.andiim.plantscan.core.designsystem.component.BasicToolbar
import com.github.andiim.plantscan.core.designsystem.theme.PlantScanTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun PlantListScreen(
    toDetails: (String) -> Unit,
    popUpScreen: () -> Unit,
    viewModel: PlantListViewModel = hiltViewModel()
) {
  PlantListContent(onItemClick = toDetails, data = viewModel.list, onBackPressed = popUpScreen)
}

@Composable
fun PlantListContent(
    onBackPressed: () -> Unit = {},
    onItemClick: (String) -> Unit = {},
    data: Flow<PagingData<com.github.andiim.plantscan.model.data.Plant>> = flowOf()
) {
  val plants = data.collectAsLazyPagingItems()
  Column {
    BasicToolbar(
        leading = {
          IconButton(onClick = onBackPressed) {
            Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.plants_exit))
          }
        },
        title = R.string.plants_label)
      com.github.andiim.plantscan.core.ui.PlantPagedList(plants = plants, onItemClick = onItemClick)
  }
}

@Preview
@Composable
private fun Preview_PlantListContent() {
  PlantScanTheme { Surface { PlantListContent() } }
}
