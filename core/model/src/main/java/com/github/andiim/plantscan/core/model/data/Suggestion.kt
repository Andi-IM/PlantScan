package com.github.andiim.plantscan.core.model.data

import java.util.Date

data class Suggestion(
    val id: String,
    val userId: String,
    val date: Date?,
    val description: String,
    val images: List<String> = listOf(),
) {
    constructor(
        userId: String = "",
        description: String = "",
        image: List<String> = listOf(),
    ) : this("", userId, null, description, image)
}
