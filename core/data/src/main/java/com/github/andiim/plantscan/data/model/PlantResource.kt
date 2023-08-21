package com.github.andiim.plantscan.data.model

import com.github.andiim.plantscan.model.data.Plant

data class PlantResource(
    val id: String,
    val name: String,
    val species: String,
    val type: String,
) {
  fun toDomain() =
      Plant(
          id = this.id,
          name = this.name,
          species = this.species,
          type = this.type,
      )
}
