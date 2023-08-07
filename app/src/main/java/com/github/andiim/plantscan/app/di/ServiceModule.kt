package com.github.andiim.plantscan.app.di

import com.github.andiim.plantscan.app.data.firebase.AccountService
import com.github.andiim.plantscan.app.data.firebase.ConfigurationService
import com.github.andiim.plantscan.app.data.firebase.LogService
import com.github.andiim.plantscan.app.data.firebase.PlantDatabase
import com.github.andiim.plantscan.app.data.firebase.implement.AccountServiceImpl
import com.github.andiim.plantscan.app.data.firebase.implement.ConfigurationServiceImpl
import com.github.andiim.plantscan.app.data.firebase.implement.LogServiceImpl
import com.github.andiim.plantscan.app.data.firebase.implement.PlantDatabaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(service: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideLogService(service: LogServiceImpl): LogService

    @Binds
    abstract fun providePlantDatabase(service: PlantDatabaseImpl): PlantDatabase

    @Binds
    abstract fun provideConfigurationService(service: ConfigurationServiceImpl): ConfigurationService
}