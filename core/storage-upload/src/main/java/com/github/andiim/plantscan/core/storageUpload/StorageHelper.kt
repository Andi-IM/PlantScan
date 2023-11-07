package com.github.andiim.plantscan.core.storageUpload

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface StorageHelper {
    fun upload(
        image: Bitmap,
        baseLocation: String,
        onProgress: ((Double) -> Double)? = null,
    ): Flow<String>
}
