package com.github.andiim.plantscan.core.firestore.di

import com.github.andiim.plantscan.core.firestore.FirebaseDataSource
import com.github.andiim.plantscan.core.firestore.fake.FakeFirebaseDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FlavoredFirestoreModule {
    @Binds
    fun binds(fakeFirebase: FakeFirebaseDataSource): FirebaseDataSource
}