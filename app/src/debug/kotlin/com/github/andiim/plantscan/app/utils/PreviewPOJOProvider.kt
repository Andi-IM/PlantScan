package com.github.andiim.plantscan.app.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.github.andiim.plantscan.app.core.domain.model.Plant

class PlantPreviewParameterProvider : PreviewParameterProvider<Plant> {
    override val values = sequenceOf(
        DataDummy.PLANTS[0]
    )
}