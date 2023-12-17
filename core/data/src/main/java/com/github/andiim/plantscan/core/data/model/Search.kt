package com.github.andiim.plantscan.core.data.model

import com.github.andiim.plantscan.core.model.data.Plant
import com.github.andiim.plantscan.core.model.data.Search
import com.github.andiim.plantscan.core.network.model.HitsItem
import com.github.andiim.plantscan.core.network.model.QueryResponse

fun QueryResponse.asExternalModel(): Search = Search(
    hits = this.hits.map(HitsItem::asExternalModel),
)

private fun HitsItem.asExternalModel(): Plant = Plant(
    id = this.objectID,
    name = this.name,
    species = this.species,
    thumbnail = this.thumbnail,
    description = "",
    commonName = emptyList(),
    images = emptyList(),
)
