package com.github.andiim.plantscan.app.core.domain.model

data class Plant(
    val id: String,
    val name: String,
    val species: String,
    val thumbnail: String,
    val description: String,
    val commonName: List<String> = listOf(),
    val images: List<Image> = listOf(),
    val taxon: Taxonomy,
)

