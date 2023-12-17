package com.github.andiim.plantscan.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class QueryResponse(
    val hits: List<HitsItem> = emptyList(),
)

@Serializable
data class HitsItem(
    val thumbnail: String = "",
    val species: String = "",
    val name: String = "",
    val objectID: String = "",
)
