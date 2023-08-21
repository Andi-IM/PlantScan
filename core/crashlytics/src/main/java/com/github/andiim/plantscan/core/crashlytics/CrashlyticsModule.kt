package com.github.andiim.plantscan.core.crashlytics

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CrashlyticsModule {
  @Binds abstract fun bindsCrashlyticsHelper(logService: LogServiceImpl): LogService
}
