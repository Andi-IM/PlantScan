package com.github.andiim.plantscan.app.ui.screens.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme

@Composable
fun DetailScreen(
    id: String?,
    popUpScreen: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
  val isSaved = viewModel.isSaved.collectAsState(initial = false)
  viewModel.uiState.collectAsState(initial = Resource.Empty).value.let { state ->
    when (state) {
      is Resource.Empty -> {
        viewModel.getDetail(id)
        viewModel.retrieveSaveData(id)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {}
      }
      is Resource.Success -> {
        val data = state.data
        DetailContent(
            content = data,
            isSaved = isSaved.value,
            onBackClick = popUpScreen,
            onSave = viewModel::setPlantToGarden,
            onRemove = viewModel::removePlantFromGarden)
      }
      is Resource.Error -> {
        Box(modifier = Modifier.fillMaxSize().padding(8.dp), contentAlignment = Alignment.Center) {
          Text("Oops, something went wrong: ${state.message}", textAlign = TextAlign.Justify)
        }
      }
      else -> {}
    }
  }
}

@Composable
fun DetailContent(
    content: Plant? = null,
    isSaved: Boolean = false,
    onBackClick: () -> Unit = {},
    onSave: (Plant) -> Unit = {},
    onRemove: (Plant) -> Unit = {}
) {
  Column(modifier = Modifier.verticalScroll(rememberScrollState())) {}
}

@Composable fun TopContent() {}

@Preview
@Composable
private fun Preview_DetailContent() {
  PlantScanTheme { Surface { DetailContent() } }
}
