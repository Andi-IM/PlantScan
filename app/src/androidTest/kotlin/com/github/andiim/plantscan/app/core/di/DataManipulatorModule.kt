package com.github.andiim.plantscan.app.core.di

import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.AccountServiceManipulator
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.AccountServiceManipulatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataManipulatorModule {
  @Binds
  fun provideAccountServiceManipulator(
      impl: AccountServiceManipulatorImpl
  ): AccountServiceManipulator
}
