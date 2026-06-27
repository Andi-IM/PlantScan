package com.github.andiim.plantscan.core.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindsAuthHelper(authHelper: StubAuthHelper): AuthHelper
}
