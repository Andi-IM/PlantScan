package com.github.andiim.plantscan.core.firestore.di

import com.github.andiim.plantscan.core.firestore.auth.AccountService
import com.github.andiim.plantscan.core.firestore.auth.AccountServiceImpl
import com.github.andiim.plantscan.core.firestore.config.ConfigurationService
import com.github.andiim.plantscan.core.firestore.config.ConfigurationServiceImpl
import com.github.andiim.plantscan.core.firestore.firestore.FirestoreCollectionImpl
import com.github.andiim.plantscan.core.firestore.firestore.FirestoreCollections
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ServiceModule {

  @Binds fun bindConfiguration(configurationService: ConfigurationServiceImpl): ConfigurationService

  @Binds fun bindAccountService(accountService: AccountServiceImpl): AccountService

  @Binds
  fun bindFirestoreCollection(firestoreCollections: FirestoreCollectionImpl): FirestoreCollections
}
