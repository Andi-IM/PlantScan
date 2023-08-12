package com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document

import com.github.andiim.plantscan.app.core.data.source.local.entity.PlantEntity
import com.github.andiim.plantscan.app.core.domain.model.Image
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.PlantDetail

data class PlantResponse(
    val id: String = "",
    val name: String = "",
    val species: String = "",
    val type: String = "",
    var images: List<Image>? = null,
    val commonName: List<String> = listOf(),
    var detail: PlantDetail? = null
) {
  fun toDomain() =
      Plant(
          id = this.id,
          name = this.name,
          species = this.species,
          type = this.type,
          images = this.images,
          commonName = this.commonName,
          detail = this.detail)

  fun toEntity() =
      PlantEntity(
          id = this.id,
          name = this.name,
          species = this.species,
          type = this.type,
          commonName = this.commonName)
}
