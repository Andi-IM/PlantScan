package com.github.andiim.plantscan.core.firestore

import com.github.andiim.plantscan.core.firestore.firestore.FirestoreCollections
import com.github.andiim.plantscan.core.firestore.utils.Constants
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class FirestoreCollectionTest {

  @Inject lateinit var firestoreCollections: FirestoreCollections
  @Inject lateinit var firestore: FirebaseFirestore

  @Before
  fun setup() {
    runBlocking { firestore.clearPersistence().await() }
  }

  @Test
  fun useAppContext() = runTest {
    DataDummy.PLANTS.forEach { firestore.collection(Constants.PLANTS).add(it) }
    val result = firestoreCollections.getPlants().first()
    assertThat(result).isNotEmpty()
    assertEquals(DataDummy.PLANTS.size, result.size)
  }
}
