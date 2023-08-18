package com.github.andiim.plantscan.app.di

import com.github.andiim.plantscan.app.core.domain.usecase.FakePlantInteractor
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(components = [ViewModelComponent::class], replaces = [AppModule::class])
@Module
abstract class TestAppModule {
  @Binds @ViewModelScoped abstract fun provideUseCase(interactor: FakePlantInteractor): PlantUseCase
}
