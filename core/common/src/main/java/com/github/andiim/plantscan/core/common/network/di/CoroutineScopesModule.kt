package com.github.andiim.plantscan.core.common.network.di

import com.github.andiim.plantscan.core.common.network.Dispatcher
import com.github.andiim.plantscan.core.common.network.PlantScantDispatchers.IO
import com.github.andiim.plantscan.core.common.network.PlantScantDispatchers.Default
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executor
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asExecutor

@Retention(AnnotationRetention.RUNTIME) @Qualifier annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopesModule {
  @Provides
  @Singleton
  @ApplicationScope
  fun providesCoroutineScope(
      @Dispatcher(Default) dispatcher: CoroutineDispatcher,
  ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

  @Provides
  @Singleton
  @ApplicationScope
  fun providesCoroutineAsDispatcher(
    @Dispatcher(IO) dispatcher: CoroutineDispatcher,
  ): Executor = dispatcher.asExecutor()
}
