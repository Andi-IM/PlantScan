package com.github.andiim.plantscan.app.core.di

import android.content.Context
import android.util.Log
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.camera.extensions.ExtensionsManager
import androidx.camera.lifecycle.ProcessCameraProvider
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CameraProvider {
    @Provides
    @Singleton
    fun provideCameraXConfig(): CameraXConfig =
        CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
            .setCameraExecutor(Dispatchers.IO.asExecutor())
            .setMinimumLoggingLevel(Log.ERROR)
            .build()

    @Provides
    @Singleton
    fun provideCamera(@ApplicationContext context: Context): ProcessCameraProvider =
        ProcessCameraProvider.getInstance(context).get()

    @Provides
    @Singleton
    fun provideCameraExtensions(
        @ApplicationContext context: Context,
        provider: ProcessCameraProvider
    ): ExtensionsManager = ExtensionsManager.getInstanceAsync(context, provider).get()

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader =
        ImageLoader.Builder(context).crossfade(true).build()
}
