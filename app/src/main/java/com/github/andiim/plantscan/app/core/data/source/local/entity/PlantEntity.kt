package com.github.andiim.plantscan.app.core.data.source.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.andiim.plantscan.app.core.domain.model.Plant
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "plants")
data class PlantEntity(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val species: String = "",
    val type: String = "",
    val commonName: List<String>? = null,
) : Parcelable {
  companion object {
    fun mapFromDomain(plant: Plant) =
        PlantEntity(
            id = plant.id,
            name = plant.name,
            species = plant.species,
            type = plant.type,
            commonName = plant.commonName)
  }
}
