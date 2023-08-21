package com.github.andiim.plantscan.core.firestore.model

import com.github.andiim.plantscan.model.data.Image
import com.github.andiim.plantscan.model.data.Plant
import com.google.firebase.firestore.DocumentId

data class PlantCollectionData(
    @DocumentId val id: String = "",
    val name: String = "",
    val species: String = "",
    val type: String = "",
    var images: List<Image>? = null,
    val commonName: List<String>? = null,
    var detail: PlantDetailCollectionData? = null
) {
  fun toDomain() =
      Plant(
          id = this.id,
          name = this.name,
          species = this.species,
          type = this.type,
          images = this.images,
          commonName = this.commonName,
          detail = this.detail?.toDomain())
}
