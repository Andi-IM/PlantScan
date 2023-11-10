package com.github.andiim.plantscan.feature.camera.extensions

import androidx.camera.extensions.ExtensionMode
import com.github.andiim.plantscan.feature.camera.R

internal val cameraExtensionsName =
    mapOf(
        ExtensionMode.AUTO to R.string.camera_mode_auto,
        ExtensionMode.NIGHT to R.string.camera_mode_night,
        ExtensionMode.HDR to R.string.camera_mode_hdr,
        ExtensionMode.FACE_RETOUCH to R.string.camera_mode_face_retouch,
        ExtensionMode.BOKEH to R.string.camera_mode_bokeh,
        ExtensionMode.NONE to R.string.camera_mode_none,
    )
