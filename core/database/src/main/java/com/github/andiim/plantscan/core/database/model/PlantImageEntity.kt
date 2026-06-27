package com.github.andiim.plantscan.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.andiim.plantscan.core.model.data.PlantImage

@Entity(
    tableName = "plantImages",
)
data class PlantImageEntity(
    @PrimaryKey
    val plantImageId: String,
    val plantRefId: String,
    val url: String,
    val attribution: String,
    val description: String,
)

fun PlantImageEntity.asExternalModel() = PlantImage(
    id = plantImageId,
    url = url,
    attribution = attribution,
    description = description,
)
