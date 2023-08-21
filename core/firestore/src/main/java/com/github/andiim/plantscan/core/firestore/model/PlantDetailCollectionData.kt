package com.github.andiim.plantscan.core.firestore.model

import com.github.andiim.plantscan.model.data.PlantDetail
import com.github.andiim.plantscan.model.data.Taxonomy

data class PlantDetailCollectionData(
    val classification: String = "",
    val description: String = "",
    val taxonomy: Taxonomy? = null
) {
  fun toDomain() =
      PlantDetail(
          classification = taxonomy,
          description = this.description,
      )
}
