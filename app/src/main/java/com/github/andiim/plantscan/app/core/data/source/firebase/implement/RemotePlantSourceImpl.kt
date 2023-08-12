package com.github.andiim.plantscan.app.core.data.source.firebase.implement

import com.github.andiim.plantscan.app.core.data.source.firebase.RemotePlantSource
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.DbResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.ClassResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.FamilyResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.GenusResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.OrderResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PhylumResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PlantDetailResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PlantResponse
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class RemotePlantSourceImpl @Inject constructor(private val db: FirebaseFirestore) :
    RemotePlantSource {
  override suspend fun searchPlant(query: String): DbResponse<List<PlantResponse>> =
      querySnapshotHandling(db.collection(PLANT_COLLECTION).whereEqualTo("name", query).limit(10))

  override suspend fun getAllPlant(): DbResponse<List<PlantResponse>> =
      querySnapshotHandling(db.collection(PLANT_COLLECTION))

  override suspend fun getPlantDetail(id: String): DbResponse<PlantDetailResponse> =
      documentSnapshotHandling(db.collection(PLANT_DETAIL_COLLECTION).document(id))

  override suspend fun getPhylum(id: String): DbResponse<PhylumResponse> =
      documentSnapshotHandling(db.collection(PHYLUM_COLLECTION).document(id))

  override suspend fun getClass(phylumId: String, classId: String): DbResponse<ClassResponse> =
      documentSnapshotHandling(
          db.collection(PHYLUM_COLLECTION)
              .document(phylumId)
              .collection(CLASS_COLLECTION)
              .document(classId))

  override suspend fun getOrder(id: String): DbResponse<OrderResponse> =
      documentSnapshotHandling(db.collection(ORDER_COLLECTION).document(id))

  override suspend fun getFamily(orderId: String, familyId: String): DbResponse<FamilyResponse> =
      documentSnapshotHandling(
          db.collection(ORDER_COLLECTION)
              .document(orderId)
              .collection(FAMILY_COLLECTION)
              .document(familyId))
  override suspend fun getGenus(ref: String): DbResponse<GenusResponse> =
      documentSnapshotHandling(db.collection(GENUS_COLLECTION).document(ref))

  private suspend inline fun <reified T : Any> querySnapshotHandling(
      reference: CollectionReference
  ): DbResponse<List<T>> {
    return try {
      val snapshot = reference.get().await()
      val documents = snapshot.documents
      if (documents.isEmpty()) {
        DbResponse.Empty
      } else {
        val plants = snapshot.toObjects<T>()
        DbResponse.Success(plants)
      }
    } catch (e: FirebaseFirestoreException) {
      DbResponse.Error(e.toString())
    }
  }

  private suspend inline fun <reified T : Any> querySnapshotHandling(
      reference: Query
  ): DbResponse<List<T>> {
    return try {
      val snapshot = reference.get().await()
      val documents = snapshot.documents
      if (documents.isEmpty()) {
        DbResponse.Empty
      } else {
        val plants = snapshot.toObjects<T>()
        DbResponse.Success(plants)
      }
    } catch (e: FirebaseFirestoreException) {
      DbResponse.Error(e.toString())
    }
  }

  private suspend inline fun <reified T> documentSnapshotHandling(
      ref: DocumentReference
  ): DbResponse<T> {
    return try {
      val snapshot = ref.get().await()
      val documents = snapshot.data.orEmpty()
      if (documents.isEmpty()) {
        DbResponse.Empty
      } else {
        val detail = snapshot.toObject<T>()!!
        DbResponse.Success(detail)
      }
    } catch (e: FirebaseFirestoreException) {
      DbResponse.Error(e.toString())
    }
  }

  companion object {
    private const val PLANT_COLLECTION = "plants"
    private const val PHYLUM_COLLECTION = "phylum"
    private const val CLASS_COLLECTION = "classes"
    private const val ORDER_COLLECTION = "orders"
    private const val FAMILY_COLLECTION = "families"
    private const val PLANT_DETAIL_COLLECTION = "plantDetails"
    private const val GENUS_COLLECTION = "genuses"
  }
}
