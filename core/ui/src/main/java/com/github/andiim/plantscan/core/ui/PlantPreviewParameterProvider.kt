package com.github.andiim.plantscan.core.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.github.andiim.plantscan.core.model.data.Plant
import com.github.andiim.plantscan.core.model.data.PlantImage
import com.github.andiim.plantscan.core.ui.PreviewParameterData.plants

class PlantPreviewParameterProvider : PreviewParameterProvider<List<Plant>> {
    override val values: Sequence<List<Plant>> = sequenceOf(plants)
}

object PreviewParameterData {
    @Suppress("detekt:MaxLineLength")
    val plants = listOf(
        Plant(
            id = "1",
            name = "moth orchid",
            species = "Phalaenopsis",
            description = "a genus of about seventy species of plants in the family Orchidaceae. Orchids in this genus are monopodial epiphytes or lithophytes with long, coarse roots, short, leafy stems and long-lasting, flat flowers arranged in a flowering stem that often branches near the end. Orchids in this genus are native to India, Taiwan, China, Southeast Asia, New Guinea and Australia with the majority in Indonesia and the Philippines.",
            thumbnail = "https://www.houseplantsexpert.com/wp-content/uploads/2022/09/phalaenopsis_pink.jpg",
            commonName = listOf(
                "Phal",
            ),
            images = listOf(
                PlantImage(
                    id = "1",
                    url = "https://www.houseplantsexpert.com/wp-content/uploads/2022/09/phalaenopsis2.jpg",
                    attribution = "houseplantsexpert",
                    description = "flower",
                ),
            ),
        ),
    )
}
