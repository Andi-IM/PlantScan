package com.github.andiim.plantscan.core.firestore.fake.model

import com.github.andiim.plantscan.core.firestore.model.ImageDocument
import com.github.andiim.plantscan.core.firestore.utils.DateJson
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ImageJson(
    val url: String,
    val date: DateJson,
    val attribution: String,
    val desc: String,
    val description: String,
    val id: Long,
)

@Suppress("detekt:MagicNumber")
fun ImageJson.toImageDocument() = ImageDocument(
    id = id,
    url = url,
    date = Date(date.seconds * 1000 + (date.nanoseconds / 1000000)),
    attribution = attribution,
    desc = desc,
    description = description,
)
