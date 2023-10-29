package com.github.andiim.plantscan.core.network

import com.github.andiim.plantscan.core.network.model.DetectionResponse
import javax.inject.Qualifier

/**
 * Interface representing network calls to the backend.
 */
interface PsNetworkDataSource {
    suspend fun detect(@Base64String image: String, confidence: Int): DetectionResponse
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Base64String
