package com.github.andiim.plantscan.core.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.github.andiim.plantscan.core.model.data.Plant

/**
 * Defines a plant data.
 * It has one to many relationship with [PlantImageEntity].
 */
@Entity(
    tableName = "plants",
)
data class PlantEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val species: String,
    val description: String,
    val thumbnail: String,
    val commonName: List<String>,
)

data class PlantAndImages(
    @Embedded
    val plant: PlantEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "plantImageId",
    )
    val images: List<PlantImageEntity>,
)

fun PlantAndImages.asExternalModel() = Plant(
    id = plant.id,
    name = plant.name,
    species = plant.species,
    description = plant.description,
    thumbnail = plant.thumbnail,
    commonName = plant.commonName,
    images = images.map(PlantImageEntity::asExternalModel),
)
