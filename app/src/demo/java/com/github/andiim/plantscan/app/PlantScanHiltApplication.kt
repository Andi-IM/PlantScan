package com.github.andiim.plantscan.app

import android.app.Application
import com.github.andiim.plantscan.app.di.DebugModule
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PlantScanHiltApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugModule.provideTimberTree())
        }
    }
}