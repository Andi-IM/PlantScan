package com.github.andiim.plantscan.app.core.data.source.firebase

import com.github.andiim.plantscan.app.BuildConfig
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.ConfigurationService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.trace
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import com.github.andiim.plantscan.app.R.xml as AppConfig

class ConfigurationServiceImpl @Inject constructor() :
    ConfigurationService {
  private val remoteConfig
    get() = Firebase.remoteConfig

  init {
    if (BuildConfig.DEBUG) {
      val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 0 }
      remoteConfig.setConfigSettingsAsync(configSettings)
    }
    remoteConfig.setDefaultsAsync(AppConfig.remote_config_defaults)
  }

  override suspend fun fetchConfiguration(): Boolean =
      trace(FETCH_CONFIG_TRACE) { remoteConfig.fetchAndActivate().await() }

  override val isShowTaskEditButtonConfig: Boolean
    get() = remoteConfig[SHOW_XX].asBoolean()
  override val mlModelName: String
    get() = remoteConfig[ML_MODEL].asString()

  companion object {
    private const val SHOW_XX = "testing"
    private const val ML_MODEL = "ml_model"
    private const val FETCH_CONFIG_TRACE = "fetchConfig"
  }
}
