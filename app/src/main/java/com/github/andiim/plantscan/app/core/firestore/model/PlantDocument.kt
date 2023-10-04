package com.github.andiim.plantscan.app.core.firestore.model

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

    data class CommonName(val name: String = "")

    constructor(
        id: String,
        taxon: TaxonomyDocument,
        description: String,
        species: String,
        name: String,
        images: List<ImageDocument> = listOf(),
        thumbnail: String,
        commonName: List<String> = listOf(),
    ) : this(id, taxon, description, null, species, null, name, images, thumbnail) {
        this.commonName = commonName.map { CommonName(it) }
    }
}
