package com.github.andiim.plantscan.core.storageUpload

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {
    @Binds
    abstract fun bindsStorageHelper(
        storageHelperImpl: FirebaseStorageHelper,
    ): StorageHelper

    companion object {
        private const val HOST = "localhost"
        private const val PORT = 9199

        @Provides
        @Singleton
        fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage.also {
            it.useEmulator(HOST, PORT)
        }
    }
}
