package com.github.andiim.plantscan.core.firestore.model

import com.github.andiim.plantscan.model.data.PlantType
import com.google.firebase.firestore.DocumentId

data class PlantTypeCollectionData(
    @DocumentId val id: String = "",
    val alias: String = "",
    val name: String = ""
) {
  fun toDomain() = PlantType(this.id, this.alias, this.name)
}
