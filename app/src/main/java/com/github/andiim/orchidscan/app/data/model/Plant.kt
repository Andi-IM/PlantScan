package com.github.andiim.orchidscan.app.data.model

data class Plant(
    val id: String = "",
    val name: String = "",
    val species: String = "",
    val type: String = "",
    var images : List<Image>? = null,
    val commonName: List<String> = listOf(),
    var detail: PlantDetail? = null
)
