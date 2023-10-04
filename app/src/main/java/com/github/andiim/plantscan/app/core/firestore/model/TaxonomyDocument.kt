package com.github.andiim.plantscan.app.core.firestore.model

import com.google.firebase.firestore.PropertyName

data class TaxonomyDocument(
    val phylum: String = "",
    val order: String = "",
    val family: String = "",
    val genus: String = ""
) {
    @get:PropertyName("class")
    @set:PropertyName("class")
    var className: String = ""

    constructor(
        phylum: String,
        order: String,
        family: String,
        genus: String,
        className: String
    ) : this(phylum, order, family, genus) {
        this.className = className
    }
}

