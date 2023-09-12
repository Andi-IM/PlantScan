package com.github.andiim.plantscan.app.ui.common

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.github.andiim.plantscan.app.core.domain.model.ObjectDetection
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.ui.screens.home.history.HistoryUiState
import com.github.andiim.plantscan.app.utils.DataDummy

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

class DetectScreenPreviewParameterProvider : PreviewParameterProvider<ObjectDetection> {
    override val values = sequenceOf(
        DataDummy.OBJECT_DETECTIONS[0]
    )
}
