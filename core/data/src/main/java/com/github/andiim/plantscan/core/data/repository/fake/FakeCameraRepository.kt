package com.github.andiim.plantscan.core.data.repository.fake

import android.graphics.Bitmap
import com.github.andiim.plantscan.core.data.repository.CameraRepository
import javax.inject.Inject

/**
 * Fake implementation of the [CameraRepository].
 */
class FakeCameraRepository @Inject constructor() : CameraRepository {
    override suspend fun savePhotoToGallery(capturePhotoBitmap: Bitmap): Result<Unit> =
        Result.success(Unit)
}
