package com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document

import com.github.andiim.plantscan.app.core.domain.model.Image

data class ImageResponse(
    val url: String = "",
    val date: String = "",
    val description: String = "",
    val attribution: String = "",
) {
    fun toModel() = Image(
        url = this.url,
        date = this.date,
        description = this.description,
        attribution = this.attribution
    )
}
