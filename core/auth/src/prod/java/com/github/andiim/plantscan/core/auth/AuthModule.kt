package com.github.andiim.plantscan.core.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindsAuthHelper(
        authHelperImpl: FirebaseAuthHelper,
    ): AuthHelper

    companion object {
        private const val HOST = "10.0.2.2"
        private const val PORT = 9099

        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth {
            return Firebase.auth.also {
                if (BuildConfig.USE_EMULTAOR.toBoolean()) {
                    it.useEmulator(HOST, PORT)
                }
            }
        }
    }
}
