package com.github.andiim.plantscan.core.firestore.di

import android.content.Context
import com.github.andiim.plantscan.core.firestore.BuildConfig
import com.github.andiim.plantscan.core.firestore.fake.FakeAssetManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MemoryCacheSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    @Provides
    @Singleton
    fun providesFakeAssetManager(
        @ApplicationContext context: Context,
    ): FakeAssetManager = FakeAssetManager(context.assets::open)

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore.also {
        if (BuildConfig.USE_EMULTAOR.toBoolean()) {
            it.useEmulator(HOST, PORT)
            it.firestoreSettings = firestoreSettings {
                setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
            }
        }
    }

    private const val HOST = "10.0.2.2"
    private const val PORT = 8080
}
