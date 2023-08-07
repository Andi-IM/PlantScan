package com.github.andiim.orchidscan.app.di

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