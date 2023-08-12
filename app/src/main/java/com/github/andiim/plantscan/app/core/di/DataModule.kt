package com.github.andiim.plantscan.app.core.di

import com.github.andiim.plantscan.app.core.data.PlantRepositoryImpl
import com.github.andiim.plantscan.app.core.data.source.firebase.RemotePlantSource
import com.github.andiim.plantscan.app.core.data.source.firebase.implement.RemotePlantSourceImpl
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
  @Binds fun provideRemoteDataSource(dataSource: RemotePlantSourceImpl) : RemotePlantSource

  @Binds fun provideRepository(repositoryImpl: PlantRepositoryImpl): PlantRepository
}
