package com.github.andiim.plantscan.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import com.github.andiim.plantscan.core.model.data.Plant

/**
 * Fts entity for the plant. @see https://developer.android.com/reference/androidx/room/Fts4.
 */
@Entity(tableName = "plantFts")
@Fts4
data class PlantFtsEntity(
    @ColumnInfo(name = "plantId")
    val plantId: String,
    val name: String,
    val species: String,
    val description: String,
)

fun PlantAndImages.asFtsEntity() = PlantFtsEntity(
    plantId = plant.id,
    name = plant.name,
    species = plant.species,
    description = plant.description,
)

fun Plant.asFtsEntity() = PlantFtsEntity(
    plantId = id,
    name = name,
    species = species,
    description = description,
)
