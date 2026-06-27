package com.github.andiim.plantscan.core.firestore.fake.model

import com.github.andiim.plantscan.core.firestore.model.PlantDocument
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlantJson(
    val id: String,
    val name: String,
    val species: String,
    val description: String,
    val thumbnail: String,
    @SerialName("common_name")
    val commonName: List<Map<String, String>>,
    val images: List<ImageJson>,
)

fun PlantJson.toDocument() = PlantDocument(
    id = id,
    name = name,
    species = species,
    description = description,
    thumbnail = thumbnail,
    commonName = commonName.map { PlantDocument.CommonName(it["name"]!!) },
    images = images.map { it.toImageDocument() },
)
