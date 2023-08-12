package com.github.andiim.plantscan.app.core.domain.model

import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PlantResponse

data class Plant(
    val id: String = "",
    val name: String = "",
    val species: String = "",
    val type: String = "",
    var images: List<Image>? = null,
    val commonName: List<String> = listOf(),
    var detail: PlantDetail? = null
) {
  companion object {
    fun mapFromResponse(data: PlantResponse) =
        Plant(
            id = data.id,
            name = data.name,
            species = data.species,
            type = data.type,
            images = data.images,
            commonName = data.commonName,
            detail = data.detail)

    fun mapFromResponse(data: List<PlantResponse>): List<Plant> {
      val list = mutableListOf<Plant>()

      data.forEach { item ->
        val plant =
            Plant(
                id = item.id,
                name = item.name,
                species = item.species,
                type = item.type,
                images = item.images,
                commonName = item.commonName,
                detail = item.detail)
        list.add(plant)
      }

      return list
    }
  }
}
