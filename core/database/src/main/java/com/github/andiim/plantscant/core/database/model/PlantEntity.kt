package com.github.andiim.plantscant.core.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.andiim.plantscan.model.data.Plant
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
  constructor(
      plant: Plant
  ) : this(
      id = plant.id,
      name = plant.name,
      species = plant.species,
      type = plant.type,
      commonName = plant.commonName,
  )

  fun asExternalModel() =
      Plant(
          id = this.id,
          name = this.name,
          species = this.species,
          type = this.type,
          commonName = this.commonName,
      )
}
