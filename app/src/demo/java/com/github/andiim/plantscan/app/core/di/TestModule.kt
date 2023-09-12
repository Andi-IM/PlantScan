package com.github.andiim.plantscan.app.core.di

import android.content.Context
import com.github.andiim.plantscan.app.core.data.source.network.fake.FakeAssetManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.InputStream
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestModule {
    @Provides
    @Singleton
    fun providesFakeAssetManager(
        @ApplicationContext context: Context,
    ): FakeAssetManager {
        val openAssets: (String) -> InputStream = context.assets::open
        return FakeAssetManager(openAssets)
    }
}