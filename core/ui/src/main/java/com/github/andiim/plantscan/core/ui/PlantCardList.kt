package com.github.andiim.plantscan.core.ui

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import com.github.andiim.plantscan.core.analytics.LocalAnalyticsHelper
import com.github.andiim.plantscan.core.model.data.Plant

/**
 * Extension function for displaying a [List] of [PlantCard] backed by a list of [Plant]s.
 */
fun LazyListScope.plantCardItems(
    items: List<Plant>,
    onClick: (String) -> Unit,
    itemModifier: Modifier = Modifier,
) = items(
    items = items,
    key = { it.id },
    itemContent = { plant ->
        val analyticsHelper = LocalAnalyticsHelper.current
        PlantCard(
            plant = plant,
            onClick = {
                analyticsHelper.logPlantOpened(plant.id)
                onClick.invoke(plant.id)
            },
            modifier = itemModifier,
        )
    },
)
