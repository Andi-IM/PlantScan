package com.github.andiim.plantscan.core.firestore.di

import com.github.andiim.plantscan.core.firestore.utils.Constants.GENUSES
import com.github.andiim.plantscan.core.firestore.utils.Constants.PLANTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
  @Provides fun auth(): FirebaseAuth = Firebase.auth

  @Provides fun firestore(): FirebaseFirestore = Firebase.firestore

}
