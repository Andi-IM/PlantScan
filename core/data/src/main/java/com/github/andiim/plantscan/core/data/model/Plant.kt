package com.github.andiim.plantscan.core.data.model

import com.github.andiim.plantscan.core.firestore.model.ImageDocument
import com.github.andiim.plantscan.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.core.model.data.Plant
import com.github.andiim.plantscan.core.model.data.PlantImage

fun PlantDocument.asExternalModel() = Plant(
    id = id,
    name = name,
    species = species,
    description = description,
    thumbnail = thumbnail,
    commonName = commonName.map { it.name },
    images = images.map { it.asExternalModel() },
)

fun ImageDocument.asExternalModel() = PlantImage(
    id = id.toString(),
    attribution = attribution ?: "",
    url = url,
    description = description ?: desc ?: "",
)
