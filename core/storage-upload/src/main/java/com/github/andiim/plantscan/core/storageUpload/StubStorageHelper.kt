package com.github.andiim.plantscan.core.storageUpload

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class StubStorageHelper @Inject constructor() : StorageHelper {
    override fun upload(
        image: Bitmap,
        baseLocation: String,
        onProgress: ((Double) -> Double)?,
    ): Flow<String> {
        return flowOf("")
    }
}
