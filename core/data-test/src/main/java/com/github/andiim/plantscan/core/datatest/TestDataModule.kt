package com.github.andiim.plantscan.core.datatest

import com.github.andiim.plantscan.data.di.DataModule
import com.github.andiim.plantscan.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DataModule::class])
interface TestDataModule {
  @Binds
  fun bindsNetworkMonitor(
      networkMonitor: AlwaysOnlineNetworkMonitor,
  ): NetworkMonitor
}
