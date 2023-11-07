package com.github.andiim.plantscan.app

import android.app.Application
import androidx.camera.core.CameraXConfig
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.github.andiim.plantscan.app.di.DebugModule
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class PlantscanApplication : Application(), CameraXConfig.Provider, Configuration.Provider {
    @Inject
    lateinit var cameraConfig: CameraXConfig

    override fun getCameraXConfig(): CameraXConfig = cameraConfig

    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(hiltWorkerFactory).build()
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugModule.provideTimberTree())
        }
    }
}
