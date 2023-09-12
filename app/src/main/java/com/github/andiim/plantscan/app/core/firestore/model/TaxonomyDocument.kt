package com.github.andiim.plantscan.app.core.firestore.model

import com.github.andiim.plantscan.app.core.domain.model.Taxonomy
import com.google.firebase.firestore.PropertyName

data class TaxonomyDocument(
    val phylum: String = "",
    val order: String = "",
    val family: String = "",
    val genus: String = ""
) {
    constructor(t: Taxonomy) : this(t.phylum, t.order, t.family, t.genus)

    @get:PropertyName("class")
    @set:PropertyName("class")
    var className: String = ""

    fun toModel(): Taxonomy = Taxonomy(
        phylum = this.phylum,
        className = this.className,
        order = this.order,
        family = this.family,
        genus = this.genus
    )
}
