package com.github.andiim.plantscan.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DebugModule {
    @Provides
    fun provideTimberTree(): TimberDebugTree = TimberDebugTree()
}
