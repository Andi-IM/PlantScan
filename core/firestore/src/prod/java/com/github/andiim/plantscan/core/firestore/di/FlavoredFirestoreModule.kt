package com.github.andiim.plantscan.core.firestore.di

import com.github.andiim.plantscan.core.firestore.PsFirebaseDataSource
import com.github.andiim.plantscan.core.firestore.network.PsFirebaseNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FlavoredFirestoreModule {
    @Binds
    fun binds(firebase: PsFirebaseNetwork): PsFirebaseDataSource
}
