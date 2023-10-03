package com.github.andiim.plantscan.app.core.firestore.model

import com.github.andiim.plantscan.app.core.domain.model.Image
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.datetime.toInstant
import java.util.Date

data class ImageDocument(
    val url: String = "",
    @ServerTimestamp val date: Date? = null,
    val attribution: String? = "",
    @Exclude val id: Long? = null,
) {
    constructor(i: Image) : this(i.url, i.date?.toDate(), i.attribution, null)

    @get:PropertyName("desc")
    @set:PropertyName("desc")
    var description: String = ""

    fun toModel() = Image(
        url = this.url,
        date = this.date?.toInstant()?.toString()?.toInstant()!!,
        description = this.description,
        attribution = this.attribution
    )

    companion object {
        fun fromModel(image: Image) = ImageDocument(url = image.url, date = image.date?.toDate()!!)
    }
}
