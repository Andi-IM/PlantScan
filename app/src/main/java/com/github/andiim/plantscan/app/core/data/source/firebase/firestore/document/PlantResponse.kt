package com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document

import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.google.firebase.firestore.DocumentId

data class PlantResponse(
    @DocumentId val id: String = "",
    val name: String = "",
    val species: String = "",
    val thumbnail: String = "",
    val description: String = "",
    val commonName: List<String> = listOf(),
    val images: List<ImageResponse> = listOf(),
    val taxon: TaxonomyResponse,
) {
    fun toDomain(): Plant =
        Plant(
            id = this.id,
            name = this.name,
            species = this.species,
            thumbnail = this.thumbnail,
            description = this.description,
            commonName = this.commonName,
            images = this.images.map {
                it.toModel()
            },
            taxon = this.taxon.toModel()
        )
}



