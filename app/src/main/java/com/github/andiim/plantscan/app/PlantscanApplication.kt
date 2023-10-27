package com.github.andiim.plantscan.app

import android.app.Application
import android.util.Log
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.github.andiim.plantscan.app.di.DebugModule
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class PlantscanApplication : Application(), CameraXConfig.Provider, Configuration.Provider {
    override fun getCameraXConfig(): CameraXConfig {
        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
            .setMinimumLoggingLevel(Log.ERROR).build()
    }

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
