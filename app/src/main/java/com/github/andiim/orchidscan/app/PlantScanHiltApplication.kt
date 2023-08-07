package com.github.andiim.orchidscan.app

import android.app.Application
import com.github.andiim.orchidscan.app.di.DebugModule.provideTimberTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PlantScanHiltApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(provideTimberTree())
        }
    }
}