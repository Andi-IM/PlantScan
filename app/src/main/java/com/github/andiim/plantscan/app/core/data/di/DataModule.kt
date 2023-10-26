package com.github.andiim.plantscan.app.core.data.di

import com.github.andiim.plantscan.core.data.repository.DefaultSuggestionRepository
import com.github.andiim.plantscan.core.data.repository.SuggestionRepository
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
    fun bindsSuggestionRepository(
        suggestionRepository: com.github.andiim.plantscan.core.data.repository.DefaultSuggestionRepository
    ): com.github.andiim.plantscan.core.data.repository.SuggestionRepository
}