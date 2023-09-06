package com.github.andiim.plantscan.app.core.data.source.network

import com.github.andiim.plantscan.app.core.data.source.network.model.DetectionResponse
import java.io.File

interface NetworkDataSource {
    suspend fun detect(image: File): DetectionResponse
}