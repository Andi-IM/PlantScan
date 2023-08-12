package com.github.andiim.plantscan.app.di

import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.AccountService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.ConfigurationService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.core.data.source.firebase.FakeAccountServiceImpl
import com.github.andiim.plantscan.app.core.data.source.firebase.FakeConfigurationServiceImpl
import com.github.andiim.plantscan.app.core.data.source.firebase.FakeLogServiceImpl
import com.github.andiim.plantscan.app.core.domain.usecase.PlantInteractor
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {
  @Binds @ViewModelScoped abstract fun provideUseCase(interactor: PlantInteractor): PlantUseCase

  @Binds
  @ViewModelScoped
  abstract fun provideAccountService(service: FakeAccountServiceImpl): AccountService

  @Binds @ViewModelScoped abstract fun provideLogService(service: FakeLogServiceImpl): LogService

  @Binds
  @ViewModelScoped
  abstract fun provideConfigurationService(
      service: FakeConfigurationServiceImpl
  ): ConfigurationService
}
