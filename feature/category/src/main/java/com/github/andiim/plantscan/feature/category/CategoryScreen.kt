package com.github.andiim.plantscan.feature.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.github.andiim.plantscan.core.designsystem.component.BasicToolbar
import com.github.andiim.plantscan.core.designsystem.theme.PlantScanTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun PlantCategoryScreen(
    onBackClick: () -> Unit,
    toPlantListScreen: (String) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
  Column(modifier = Modifier.fillMaxSize()) {
    BasicToolbar(
        leading = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.plants_exit))
          }
        },
        title = R.string.plants_label)

    PlantCategories(data = viewModel.types, onTypeClick = toPlantListScreen)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantCategories(
    data: Flow<PagingData<com.github.andiim.plantscan.model.data.PlantType>> = flowOf(),
    onTypeClick: (String) -> Unit = {}
) {
  val saveType = rememberSaveable { mutableStateOf("") }
  val types = data.collectAsLazyPagingItems()

  if (types.itemCount == 0) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text("Empty Item!")
    }
  } else {
    LazyVerticalGrid(
        state = rememberLazyGridState(),
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.fillMaxSize()) {
          items(
              count = types.itemCount,
              key = types.itemKey { it.id },
              contentType = types.itemContentType { "Categories" }) { index ->
                val typeData = types[index]
                typeData?.let { type ->
                  Card(
                      modifier = Modifier.padding(4.dp).height(100.dp),
                      onClick = { saveType.value = type.name }) {
                        Text("Name = ${type.name}")
                        Text(type.alias)
                      }
                }
              }
        }
  }
}

@Preview
@Composable
private fun Preview_PlantEmptyCategories() {
  PlantScanTheme { Surface { PlantCategories() } }
}
