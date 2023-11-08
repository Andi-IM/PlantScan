package com.github.andiim.plantscan.core.firestore.model

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
    val name: String = "",
    val images: List<ImageDocument> = listOf(),
    val thumbnail: String = "",
) {
    @get:PropertyName("common_name")
    @set:PropertyName("common_name")
    var commonName: List<CommonName> = listOf()

    @[Exclude ServerTimestamp]
    @get:PropertyName("Updated")
    @set:PropertyName("Updated")
    var updated: Date? = null

    data class CommonName(val name: String = "")
}
