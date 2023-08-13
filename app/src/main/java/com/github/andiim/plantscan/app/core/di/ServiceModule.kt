package com.github.andiim.plantscan.app.core.di

import com.github.andiim.plantscan.app.core.data.source.firebase.AccountServiceImpl
import com.github.andiim.plantscan.app.core.data.source.firebase.ConfigurationServiceImpl
import com.github.andiim.plantscan.app.core.data.source.firebase.LogServiceImpl
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.AccountService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.ConfigurationService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ServiceModule {
  @Binds abstract fun provideAccountService(service: AccountServiceImpl): AccountService
  @Binds abstract fun provideLogService(service: LogServiceImpl): LogService
  @Binds
  abstract fun provideConfigurationService(service: ConfigurationServiceImpl): ConfigurationService
}
