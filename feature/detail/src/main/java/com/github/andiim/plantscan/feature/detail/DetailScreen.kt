package com.github.andiim.plantscan.feature.detail

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.andiim.plantscan.core.designsystem.theme.PlantScanTheme
import com.github.andiim.plantscan.model.data.Plant
import org.jetbrains.annotations.VisibleForTesting

@Composable
fun DetailScreen(onBackClick: () -> Unit, viewModel: DetailViewModel = hiltViewModel()) {
  val isSaved by viewModel.savedStatus.collectAsState()
  val detailUiState: DetailUiState by viewModel.detailUiState.collectAsStateWithLifecycle()

  DetailContent(
      isSaved = isSaved,
      onBackClick = onBackClick,
      detailUiState = detailUiState,
      onSaveClick = viewModel::setPlantToGarden)
}

@VisibleForTesting
@Composable
fun DetailContent(
    isSaved: Boolean = false,
    detailUiState: DetailUiState,
    onBackClick: () -> Unit,
    onSaveClick: (Plant) -> Unit,
) {
  val state = rememberLazyListState()
}

@Preview
@Composable
private fun Preview_DetailContent() {
  PlantScanTheme {
    DetailContent(
        isSaved = false,
        detailUiState = DetailUiState.Success(plantDetail = Plant()),
        onBackClick = {},
        onSaveClick = {})
  }
}
