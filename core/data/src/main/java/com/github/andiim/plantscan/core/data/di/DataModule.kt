package com.github.andiim.plantscan.core.data.di

import com.github.andiim.plantscan.core.data.repository.CameraRepository
import com.github.andiim.plantscan.core.data.repository.DefaultCameraRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Singleton
    @Binds
    fun bindsCameraRepository(
        cameraRepository: DefaultCameraRepository
    ): CameraRepository

}