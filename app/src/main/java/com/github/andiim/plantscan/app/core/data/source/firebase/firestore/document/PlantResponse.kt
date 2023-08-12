package com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document

import com.github.andiim.plantscan.app.core.domain.model.Image
import com.github.andiim.plantscan.app.core.domain.model.PlantDetail

data class PlantResponse(
    val id: String = "",
    val name: String = "",
    val species: String = "",
    val type: String = "",
    var images : List<Image>? = null,
    val commonName: List<String> = listOf(),
    var detail: PlantDetail? = null
)