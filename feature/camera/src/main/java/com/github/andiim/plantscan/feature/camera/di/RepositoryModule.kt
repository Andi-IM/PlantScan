package com.github.andiim.plantscan.feature.camera.di

import com.github.andiim.plantscan.feature.camera.repository.CameraRepository
import com.github.andiim.plantscan.feature.camera.repository.DefaultCameraRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
  @Binds
  @ViewModelScoped
  fun bindCameraRepository(cameraRepository: DefaultCameraRepository): CameraRepository
}
