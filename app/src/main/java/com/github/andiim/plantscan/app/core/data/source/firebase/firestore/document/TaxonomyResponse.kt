package com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document

data class TaxonomyResponse(
    val id: String = "",
    val phylum: String = "",
    val className: String = "",
    val order: String = "",
    val family: String = "",
    val genus: String = ""
)

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
