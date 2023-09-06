package com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document

import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
import kotlinx.datetime.toInstant

data class PlantResponse(
    @DocumentId val id: String = "",
    val taxon: TaxonomyResponse,
    val species: String = "",
    val name: String = "",
    val images: List<ImageResponse> = listOf(),
    val commonName: List<String> = listOf(),
    val thumbnail: String = "",
    @ServerTimestamp val updated: Date? = null,
    val description: String = "",
) {
    fun toModel(): Plant =
        Plant(
            id = this.id,
            taxon = this.taxon.toModel(),
            species = this.species,
            name = this.name,
            images = this.images.map {
                it.toModel()
            },
            commonName = this.commonName,
            thumbnail = this.thumbnail,
            updated = this.updated?.toString()?.toInstant()!!,
            description = this.description,
        )
}



