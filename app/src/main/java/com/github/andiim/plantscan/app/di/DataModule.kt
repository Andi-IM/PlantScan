package com.github.andiim.plantscan.app.di

import com.github.andiim.plantscan.app.core.data.PlantRepositoryImpl
import com.github.andiim.plantscan.app.core.data.source.firebase.FirestoreSourceImpl
import com.github.andiim.plantscan.app.core.data.util.ConnectivityManagerNetworkMonitor
import com.github.andiim.plantscan.app.core.data.util.NetworkMonitor
import com.github.andiim.plantscan.app.core.domain.repository.CameraRepository
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.FirestoreSource
import com.github.andiim.plantscan.app.ui.screens.camera.repository.CameraRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun provideRemoteDataSource(
        dataSource: FirestoreSourceImpl,
    ): FirestoreSource

    @Binds
    fun provideRepository(
        repositoryImpl: PlantRepositoryImpl,
    ): PlantRepository

    @Binds
    fun provideCameraRepository(
        repositoryImpl: CameraRepositoryImpl,
    ): CameraRepository

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor
}
