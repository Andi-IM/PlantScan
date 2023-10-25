package com.github.andiim.plantscan.app.data.di

import com.github.andiim.plantscan.app.data.CameraRepository
import com.github.andiim.plantscan.app.data.DefaultCameraRepository
import com.github.andiim.plantscan.app.data.DefaultSuggestionRepository
import com.github.andiim.plantscan.app.data.SuggestionRepository
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

    @Singleton
    @Binds
    fun bindsSuggestionRepository(
        suggestionRepository: DefaultSuggestionRepository
    ): SuggestionRepository
}