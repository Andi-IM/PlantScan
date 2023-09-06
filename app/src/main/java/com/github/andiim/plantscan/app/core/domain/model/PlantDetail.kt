package com.github.andiim.plantscan.app.core.domain.model

import com.github.andiim.plantscan.app.core.firestore.model.PlantDetailResponse

data class PlantDetail(val classification: Taxonomy? = null, val description: String? = null) {
  companion object {
    fun mapFromResponse(response: PlantDetailResponse, taxonomy: Taxonomy) =
        PlantDetail(classification = taxonomy, description = response.description)
  }
}
