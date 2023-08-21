package com.github.andiim.plantscan.model.data

data class Plant(
    val id: String = "",
    val name: String = "",
    val species: String = "",
    val type: String = "",
    var images: List<Image>? = null,
    val commonName: List<String>? = null,
    var detail: PlantDetail? = null
)