package com.github.andiim.orchidscan.app

import android.app.Application
import com.github.andiim.orchidscan.app.di.DebugModule.provideTimberTree
import timber.log.Timber

class PlantScanHiltApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(provideTimberTree())
        }
    }
}