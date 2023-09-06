package com.github.andiim.plantscan.app.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.ui.screens.home.history.HistoryUiState

class PlantPreviewParameterProvider : PreviewParameterProvider<Plant> {
    override val values = sequenceOf(
        DataDummy.PLANTS[0]
    )
}

class HistoryScreenPreviewParameterProvider : PreviewParameterProvider<HistoryUiState> {
    override val values = sequenceOf(
        HistoryUiState.Loading,
        HistoryUiState.Error("ABCABC"),
        HistoryUiState.Success(listOf())
    )
}