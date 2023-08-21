package com.github.andiim.plantscan.core.firestore.model

import com.google.firebase.firestore.DocumentId

data class TaxonomyCollectionData(
    val id: String = "",
    val phylum: String = "",
    val className: String = "",
    val order: String = "",
    val family: String = "",
    val genus: String = ""
)

data class PhylumCollectionData(
    @DocumentId val id: String,
    val name: String,
)

data class ClassCollectionData(val name: String, val orders: List<String>)

data class OrderCollectionData(val name: String)

data class GenusCollectionData(
    val id: String,
    val name: String,
    val classRef: String,
    val familyRef: String,
    val orderRef: String,
    val phylumRef: String
)

data class FamilyCollectionData(val name: String, val genuses: List<String>)
