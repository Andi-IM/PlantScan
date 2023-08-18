package com.github.andiim.plantscan.app.core.di

import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.FakeAccountServiceImpl
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.FakeConfigurationServiceImpl
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.FakeLogServiceImpl
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.AccountService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.ConfigurationService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(components = [ViewModelComponent::class], replaces = [ServiceModule::class])
@Module
abstract class ServiceTestModule {
  @Binds abstract fun provideAccountService(service: FakeAccountServiceImpl): AccountService
  @Binds abstract fun provideLogService(service: FakeLogServiceImpl): LogService
  @Binds
  abstract fun provideConfigurationService(
      service: FakeConfigurationServiceImpl
  ): ConfigurationService
}
