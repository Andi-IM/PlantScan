package com.github.andiim.plantscan.app

import android.app.Application
import com.github.andiim.plantscan.app.BuildConfig
import com.github.andiim.plantscan.app.di.DebugModule.provideTimberTree
import timber.log.Timber

class PlantScanHiltApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(provideTimberTree())
        }
    }
}