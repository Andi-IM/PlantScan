package com.github.andiim.plantscan.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AlgoliaRequest(
    val params: String,
)
