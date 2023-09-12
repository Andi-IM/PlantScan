package com.github.andiim.plantscan.app.core.domain.model

data class Plant(
    val id: String,
    val taxon: Taxonomy,
    val species: String,
    val name: String,
    val images: List<Image> = listOf(),
    val commonName: List<String> = listOf(),
    val thumbnail: String,
    val description: String,
)
