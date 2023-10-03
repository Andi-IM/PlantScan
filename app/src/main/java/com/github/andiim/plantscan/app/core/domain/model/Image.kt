package com.github.andiim.plantscan.app.core.domain.model

import kotlinx.datetime.Instant

data class Image(
    val url: String,
    val date: Instant?,
    val description: String?,
    val attribution: String?,
) {
    constructor(url: String) : this(url, null, null, null)
    constructor(url: String, time: Instant) : this(url, time, null, null)
}
