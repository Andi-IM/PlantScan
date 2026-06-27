package com.github.andiim.plantscan.app

import android.app.Application
import androidx.camera.core.CameraXConfig
import com.github.andiim.plantscan.app.di.DebugModule
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class PlantscanApplication : Application(), CameraXConfig.Provider {
    @Inject
    lateinit var cameraConfig: CameraXConfig

    override fun getCameraXConfig(): CameraXConfig = cameraConfig

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugModule.provideTimberTree())
        }
    }
}
