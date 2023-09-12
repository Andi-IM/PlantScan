package com.github.andiim.plantscan.app.core.data.source.network

import com.github.andiim.plantscan.app.core.data.source.network.model.DetectionResponse

interface NetworkDataSource {
    suspend fun detect(image: String): DetectionResponse
}
