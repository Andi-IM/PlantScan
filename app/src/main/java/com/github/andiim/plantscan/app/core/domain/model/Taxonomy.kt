package com.github.andiim.plantscan.app.core.domain.model

import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.ClassResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.FamilyResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.GenusResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.OrderResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PhylumResponse

data class Taxonomy(
    val id: String = "",
    val phylum: String = "",
    val className: String = "",
    val order: String = "",
    val family: String = "",
    val genus: String = ""
) {
  companion object {
    fun mapFromResponse(
        phylum: PhylumResponse,
        className: ClassResponse,
        order: OrderResponse,
        family: FamilyResponse,
        genus: GenusResponse
    ) =
        Taxonomy(
            id = genus.id,
            phylum = phylum.name,
            className = className.name,
            order = order.name,
            family = family.name,
            genus = genus.name)
  }
}
