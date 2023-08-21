package com.github.andiim.plantscan.model.data

/** An entity that holds the search result. */
data class SearchResult(
    val plants: List<Plant> = emptyList(),
    /** put here if another data exist */
)
