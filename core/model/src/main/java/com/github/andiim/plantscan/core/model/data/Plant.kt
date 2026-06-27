package com.github.andiim.plantscan.core.model.data

/**
 * External data layer representation of Plant data.
 */
data class Plant(
    val id: String,
    val name: String,
    val species: String,
    val description: String,
    val thumbnail: String,
    val commonName: List<String>,
    val images: List<PlantImage>
)
