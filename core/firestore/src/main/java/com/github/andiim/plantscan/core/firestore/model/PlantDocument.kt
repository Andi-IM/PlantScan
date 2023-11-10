package com.github.andiim.plantscan.core.firestore.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class PlantDocument(
    @DocumentId val id: String = "",
    val name: String = "",
    val species: String = "",
    val description: String = "",
    val thumbnail: String = "",
    @get:PropertyName("common_name")
    @set:PropertyName("common_name")
    var commonName: List<CommonName> = listOf(),
    val images: List<ImageDocument> = listOf(),
    var taxonomy: TaxonomyDocument? = null,
    @Exclude @ServerTimestamp var date: Date? = null,
    @Exclude @ServerTimestamp @get:PropertyName("Updated") @set:PropertyName("Updated")
    var updated: Date? = null,
) {
    data class CommonName(val name: String = "")

    constructor(
        id: String,
        name: String,
        species: String,
        description: String,
        thumbnail: String,
        commonName: List<CommonName>,
        images: List<ImageDocument>,
    ) : this(
        id,
        name,
        species,
        description,
        thumbnail,
        commonName,
        images,
        null,
        null,
        null,
    )
}
