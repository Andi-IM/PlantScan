package com.github.andiim.plantscan.app.core.domain.model

import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PlantResponse
import com.github.andiim.plantscan.app.core.data.source.local.entity.PlantEntity

data class Plant(
    val id: String = "",
    val name: String = "",
    val species: String = "",
    val type: String = "",
    var images: List<Image>? = null,
    val commonName: List<String> = listOf(),
    var detail: PlantDetail? = null
) {
  fun toEntity() =
      PlantEntity(
          id = this.id,
          name = this.name,
          species = this.species,
          type = this.type,
          commonName = this.commonName)

  companion object {
    fun mapFromEntity(data: PlantEntity) =
        Plant(
            id = data.id,
            name = data.name,
            species = data.species,
            type = data.type,
            commonName = data.commonName)

    fun mapFromEntity(entity: List<PlantEntity>): List<Plant> = entity.map { mapFromEntity(it) }

    fun mapFromResponse(data: PlantResponse) =
        Plant(
            id = data.id,
            name = data.name,
            species = data.species,
            type = data.type,
            images = data.images,
            commonName = data.commonName,
            detail = data.detail)

    fun mapFromResponse(data: List<PlantResponse>): List<Plant> = data.map { mapFromResponse(it) }
  }
}
