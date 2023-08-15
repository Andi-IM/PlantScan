package com.github.andiim.plantscan.app.core.domain.repository

import android.net.Uri
import java.io.File

/**
 * Manages photo capture filename and location generation. Once a photo is captured and saved to
 * disk, the repository will also notify that the image has been created such that other
 * applications can view it.
 */
interface CameraRepository {
  fun notifyImageCreated(savedUri: Uri)
  fun createImageOutputFile(): File
}
