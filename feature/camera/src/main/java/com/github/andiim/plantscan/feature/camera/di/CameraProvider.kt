package com.github.andiim.plantscan.feature.camera.di

import android.content.Context
import android.util.Log
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.camera.extensions.ExtensionsManager
import androidx.camera.lifecycle.ProcessCameraProvider
import com.github.andiim.plantscan.core.network.AppDispatchers.IO
import com.github.andiim.plantscan.core.network.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CameraProvider {
    @Provides
    @Singleton
    fun provideCameraXConfig(
        @Dispatcher(IO) ioDispatchers: CoroutineDispatcher,
    ): CameraXConfig =
        CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
            .setCameraExecutor(ioDispatchers.asExecutor())
            .setMinimumLoggingLevel(Log.ERROR)
            .build()

    @Provides
    @Singleton
    fun providesCamera(
        @ApplicationContext context: Context,
    ): ProcessCameraProvider = ProcessCameraProvider.getInstance(context).get()

    @Singleton
    @Provides
    fun providesCameraExtensions(
        @ApplicationContext context: Context,
        provider: ProcessCameraProvider,
    ): ExtensionsManager = ExtensionsManager.getInstanceAsync(context, provider).get()
}
