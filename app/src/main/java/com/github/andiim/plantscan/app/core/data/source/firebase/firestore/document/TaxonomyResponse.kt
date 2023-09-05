package com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document

import com.github.andiim.plantscan.app.core.domain.model.Taxonomy
import com.google.firebase.firestore.PropertyName

data class TaxonomyResponse(
    val phylum: String = "",
    @PropertyName("class") val className: String = "",
    val order: String = "",
    val family: String = "",
    val genus: String = ""
) {
    fun toModel(): Taxonomy = Taxonomy(
        phylum = this.phylum,
        className = this.className,
        order = this.order,
        family = this.family,
        genus = this.genus
    )
}

data class PhylumResponse(val name: String)

data class ClassResponse(val name: String, val orders: List<String>)

data class OrderResponse(val name: String)

data class GenusResponse(
    val id: String,
    val name: String,
    val classRef: String,
    val familyRef: String,
    val orderRef: String,
    val phylumRef: String
)

data class FamilyResponse(val name: String, val genuses: List<String>)
