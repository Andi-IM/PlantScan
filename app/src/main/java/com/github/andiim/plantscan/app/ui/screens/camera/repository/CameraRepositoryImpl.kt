package com.github.andiim.plantscan.app.ui.screens.camera.repository

import android.content.Context
import android.content.Intent
import android.hardware.Camera.ACTION_NEW_PICTURE
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.github.andiim.plantscan.app.core.domain.repository.CameraRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

    companion object {
        private const val PHOTO_EXTENSION = ".jpg"
        private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    }

    override fun notifyImageCreated(savedUri: Uri) {
        val file = savedUri.toFile()
        val fileProviderUri =
            FileProvider.getUriForFile(appContext, appContext.packageName + ".provider", file)

        @Suppress("DEPRECATION")
        val intent = Intent(ACTION_NEW_PICTURE, fileProviderUri)
        appContext.sendBroadcast(intent)
    }

    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

    override fun createImageOutputFile(): File {
        val filesDir = appContext.externalCacheDir
        return File.createTempFile(timeStamp, PHOTO_EXTENSION, filesDir)
    }
}
