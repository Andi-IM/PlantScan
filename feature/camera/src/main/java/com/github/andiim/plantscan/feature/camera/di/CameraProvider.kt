package com.github.andiim.plantscan.feature.camera.di

import android.content.Context
import android.util.Log
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.camera.extensions.ExtensionsManager
import androidx.camera.lifecycle.ProcessCameraProvider
import coil.ImageLoader
import com.github.andiim.plantscan.core.common.network.Dispatcher
import com.github.andiim.plantscan.core.common.network.PlantScantDispatchers.IO
import com.github.andiim.plantscan.core.common.network.di.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.Executor
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

@Module
@InstallIn(ViewModelComponent::class)
object CameraProvider {

    @Provides
    @Singleton
    fun provideCameraXConfig(@ApplicationScope executor: Executor): CameraXConfig =
        CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
            .setCameraExecutor(executor)
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