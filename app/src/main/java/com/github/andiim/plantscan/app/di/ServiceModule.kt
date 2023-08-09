package com.github.andiim.plantscan.app.di

import com.github.andiim.plantscan.app.data.firebase.AccountService
import com.github.andiim.plantscan.app.data.firebase.ConfigurationService
import com.github.andiim.plantscan.app.data.firebase.LogService
import com.github.andiim.plantscan.app.data.firebase.PlantDatabase
import com.github.andiim.plantscan.app.data.firebase.implement.FakeAccountServiceImpl
import com.github.andiim.plantscan.app.data.firebase.implement.FakeConfigurationServiceImpl
import com.github.andiim.plantscan.app.data.firebase.implement.FakeLogServiceImpl
import com.github.andiim.plantscan.app.data.firebase.implement.FakePlantDatabaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(service: FakeAccountServiceImpl): AccountService

    @Binds
    abstract fun provideLogService(service: FakeLogServiceImpl): LogService

    @Binds
    abstract fun providePlantDatabase(service: FakePlantDatabaseImpl): PlantDatabase

    @Binds
    abstract fun provideConfigurationService(service: FakeConfigurationServiceImpl): ConfigurationService
}