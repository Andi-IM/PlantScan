package com.github.andiim.plantscan.app.core.utils.di

import com.github.andiim.plantscan.app.core.utils.ConnectivityManagerNetworkMonitor
import com.github.andiim.plantscan.app.core.utils.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ConnectivityModule {
    @Binds
    fun provideConnectivityModule(
        monitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor
}