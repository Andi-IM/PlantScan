package com.github.andiim.plantscan.core.firestore.model

import com.google.firebase.firestore.PropertyName

data class TaxonomyDocument(
    val phylum: String = "",
    val order: String = "",
    val family: String = "",
    val genus: String = "",
) {
    @get:PropertyName("class")
    @set:PropertyName("class")
    var className: String = ""
}
