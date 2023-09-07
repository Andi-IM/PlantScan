package com.github.andiim.plantscan.app.core.firestore.model

import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class PlantDocument(
    @DocumentId val id: String = "",
    var taxonomy: TaxonomyDocument? = null,
    val description: String = "",
    @[Exclude ServerTimestamp] var date: Date? = null,
    val species: String = "",
    @[Exclude ServerTimestamp] var updated: Date? = null,
    val name: String = "",
    val images: List<ImageDocument> = listOf(),
    val thumbnail: String = "",
) {
    @get:PropertyName("common_name")
    @set:PropertyName("common_name")
    var commonName: List<CommonName> = listOf()

    fun toModel(): Plant =
        Plant(
            id = this.id,
            taxon = this.taxonomy?.toModel()!!,
            species = this.species,
            name = this.name,
            images = this.images.map {
                it.toModel()
            },
            commonName = this.commonName.map { it.name },
            thumbnail = this.thumbnail,
            description = this.description,
        )

    data class CommonName(val name: String = "")
}



