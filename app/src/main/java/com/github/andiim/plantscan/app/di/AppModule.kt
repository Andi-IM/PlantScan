package com.github.andiim.plantscan.app.di

import com.github.andiim.plantscan.app.core.domain.usecase.PlantInteractor
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {
    @Binds @ViewModelScoped
    abstract fun provideUseCase(interactor: PlantInteractor): PlantUseCase
}
