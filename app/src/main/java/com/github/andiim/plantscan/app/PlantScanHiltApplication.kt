package com.github.andiim.plantscan.app

import android.app.Application
import com.github.andiim.plantscan.app.di.DebugModule.provideTimberTree
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PlantScanHiltApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    Firebase.appCheck.installAppCheckProviderFactory(
        PlayIntegrityAppCheckProviderFactory.getInstance(),
    )

    if (BuildConfig.DEBUG) {
      Timber.plant(provideTimberTree())

      Firebase.appCheck.installAppCheckProviderFactory(
          DebugAppCheckProviderFactory.getInstance(),
      )
    }
  }
}
