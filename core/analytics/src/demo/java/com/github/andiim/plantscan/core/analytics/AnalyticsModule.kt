package com.github.andiim.plantscan.core.analytics

import com.github.andiim.core.analytics.AnalyticsHelper
import com.github.andiim.core.analytics.StubAnalyticsHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {
    @Binds
    abstract fun bindsAnalyticsHelper(analyticsHelperImpl: StubAnalyticsHelper): AnalyticsHelper
}
