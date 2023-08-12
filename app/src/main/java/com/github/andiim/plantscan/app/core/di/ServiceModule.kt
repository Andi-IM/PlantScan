package com.github.andiim.plantscan.app.core.di

import com.github.andiim.plantscan.app.core.data.source.firebase.AccountService
import com.github.andiim.plantscan.app.core.data.source.firebase.ConfigurationService
import com.github.andiim.plantscan.app.core.data.source.firebase.LogService
import com.github.andiim.plantscan.app.core.data.source.firebase.implement.FakeAccountServiceImpl
import com.github.andiim.plantscan.app.core.data.source.firebase.implement.FakeConfigurationServiceImpl
import com.github.andiim.plantscan.app.core.data.source.firebase.implement.FakeLogServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
  @Binds abstract fun provideAccountService(service: FakeAccountServiceImpl): AccountService

  @Binds abstract fun provideLogService(service: FakeLogServiceImpl): LogService

  @Binds
  abstract fun provideConfigurationService(
      service: FakeConfigurationServiceImpl
  ): ConfigurationService
}
