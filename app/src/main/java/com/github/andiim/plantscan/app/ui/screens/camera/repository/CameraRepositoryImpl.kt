package com.github.andiim.plantscan.app.ui.screens.camera.repository

import android.content.Context
import android.content.Intent
import android.hardware.Camera.ACTION_NEW_PICTURE
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.core.domain.repository.CameraRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages photo capture filename and location generation. Once a photo is captured and saved to
 * disk, the repository will also notify that the image has been created such that other
 * applications can view it.
 */
@Singleton
class CameraRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    CameraRepository {

    private val appContext = context
    private var rootDirectory: File

    companion object {
        private const val PHOTO_EXTENSION = ".jpg"
    }

    init {
        val mediaDir =
            context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
        rootDirectory = if (mediaDir?.exists() == true) mediaDir else appContext.filesDir
    }

    override fun notifyImageCreated(savedUri: Uri) {
        val file = savedUri.toFile()
        val fileProviderUri =
            FileProvider.getUriForFile(appContext, appContext.packageName + ".provider", file)
        appContext.sendBroadcast(Intent(ACTION_NEW_PICTURE, fileProviderUri))
    }

    override fun createImageOutputFile(): File =
        File(rootDirectory, PHOTO_EXTENSION.generateFilename())

    private fun String.generateFilename() = UUID.randomUUID().toString() + this
}
