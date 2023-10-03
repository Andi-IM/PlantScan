package com.github.andiim.plantscan.app.ui.screens.camera.viewstate

import android.net.Uri
import com.github.andiim.plantscan.app.ui.screens.camera.adapter.CameraExtensionItem
import com.github.andiim.plantscan.app.ui.screens.camera.model.CameraUiState

fun CaptureScreenViewState.handleCameraNotReady(): CaptureScreenViewState {
    return this.updatePostCaptureScreen {
        PostCaptureScreenViewState.PostCaptureScreenHiddenViewState
    }
        .updateCameraScreen {
            it.showCameraControls().enableCameraShutter(false).enableSwitchLens(false)
        }
}

fun CaptureScreenViewState.handleCameraReady(
    cameraUiState: CameraUiState,
    camExtItems: (List<Int>) -> List<CameraExtensionItem>,
): CaptureScreenViewState {
    return this.updateCameraScreen { s ->
        s.showCameraControls()
            .setAvailableExtensions(camExtItems(cameraUiState.availableExtensions))
    }
}

fun CaptureScreenViewState.handleCaptureNotReady(): CaptureScreenViewState {
    return this.updatePostCaptureScreen {
        PostCaptureScreenViewState.PostCaptureScreenHiddenViewState
    }
        .updateCameraScreen {
            it.enableBackButton(true)
                .enableCameraShutter(true)
                .enableSwitchLens(true)
                .enableGalleryButton(true)
        }
}

fun CaptureScreenViewState.handleCaptureReady(): CaptureScreenViewState {
    return this.updateCameraScreen {
        it.enableBackButton(true)
            .enableCameraShutter(true)
            .enableSwitchLens(true)
            .enableGalleryButton(true)
    }
}

fun CaptureScreenViewState.handleOpenGallery(): CaptureScreenViewState {
    return this.updateCameraScreen {
        it.enableBackButton(false)
            .enableCameraShutter(false)
            .enableSwitchLens(false)
            .enableGalleryButton(false)
    }
}

fun CaptureScreenViewState.handleCaptureStarted(): CaptureScreenViewState {
    return this.updateCameraScreen {
        it.enableBackButton(false)
            .enableCameraShutter(false)
            .enableSwitchLens(false)
            .enableGalleryButton(false)
    }
}

fun CaptureScreenViewState.handleImageObtained(uri: Uri?): CaptureScreenViewState {
    return this.updatePostCaptureScreen {
        if (uri != null) {
            PostCaptureScreenViewState.PostCaptureScreenVisibleViewState(uri)
        } else {
            PostCaptureScreenViewState.PostCaptureScreenHiddenViewState
        }
    }
        .updateCameraScreen { it.hideCameraControls() }
}

fun CaptureScreenViewState.handleFinishedCapture(uri: Uri?): CaptureScreenViewState {
    return this.updatePostCaptureScreen {
        if (uri != null) {
            PostCaptureScreenViewState.PostCaptureScreenVisibleViewState(uri)
        } else {
            PostCaptureScreenViewState.PostCaptureScreenHiddenViewState
        }
    }
        .updateCameraScreen { it.hideCameraControls() }
}

fun CaptureScreenViewState.handleFailedCapture(): CaptureScreenViewState {
    return this.updateCameraScreen {
        it.showCameraControls().enableCameraShutter(true).enableSwitchLens(true)
    }
}
