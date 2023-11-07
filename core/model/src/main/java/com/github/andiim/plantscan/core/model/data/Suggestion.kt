package com.github.andiim.plantscan.core.model.data

import java.util.Date

data class Suggestion(
    val id: String,
    val userId: String,
    val date: Date,
    val description: String,
    val imageBase64: List<String> = listOf(),
)
