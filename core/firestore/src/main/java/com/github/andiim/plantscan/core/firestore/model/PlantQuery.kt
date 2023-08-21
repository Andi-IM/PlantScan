package com.github.andiim.plantscan.core.firestore.model

data class PlantQuery(
    /** Plant types to filter for. Null means any plant type will match. */
    val filterPlantType: MutableList<String>? = mutableListOf(),
    /** Plant ids to filter for. Null means any plants id will match. */
    val filterPlantIds: MutableList<String>? = mutableListOf(),
    val filterPlantNames: MutableList<String>? = mutableListOf()
)
