package com.github.andiim.plantscan.core.data.di

import com.github.andiim.plantscan.core.data.model.UserDataRepository
import com.github.andiim.plantscan.core.data.repository.CameraRepository
import com.github.andiim.plantscan.core.data.repository.DefaultCameraRepository
import com.github.andiim.plantscan.core.data.repository.DefaultUserDataRepository
import com.github.andiim.plantscan.core.data.util.ConnectivityManagerNetworkMonitor
import com.github.andiim.plantscan.core.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsUserDataRepository(
        userDataRepository: DefaultUserDataRepository,
    ): UserDataRepository

    @Binds
    fun bindsCameraRepository(
        cameraRepository: DefaultCameraRepository
    ): CameraRepository

    @Binds
    fun bindsNetworkModule(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor
}