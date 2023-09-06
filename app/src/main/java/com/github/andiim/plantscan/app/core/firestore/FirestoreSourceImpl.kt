package com.github.andiim.plantscan.app.core.firestore

import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.trace
import com.github.andiim.plantscan.app.core.firestore.model.DetectionHistoryDocument
import com.github.andiim.plantscan.app.core.firestore.model.PlantResponse
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirestoreSourceImpl @Inject constructor(private val db: FirebaseFirestore) :
    FirestoreSource {

    override suspend fun getPlants(query: String, limit: Long): List<PlantResponse> =
        querySnapshotHandling(
            db.collection(PLANT_COLLECTION).whereGreaterThanOrEqualTo("name", query).limit(limit)
        )

    override suspend fun getPlantById(id: String): PlantResponse =
        documentSnapshotHandling(db.collection(PLANT_COLLECTION).document(id))

    override suspend fun recordDetection(detection: DetectionHistoryDocument): String =
        trace(SAVE_DETECT_TRACE) {
            db.collection(DETECT_COLLECTION).add(detection).await().id
        }

    override suspend fun getDetectionsList(): List<DetectionHistoryDocument> =
        querySnapshotHandling(db.collection(DETECT_COLLECTION))


    private suspend inline fun <reified T : Any> querySnapshotHandling(
        reference: CollectionReference
    ): List<T> {
        try {
            val snapshot = reference.get().await()
            return snapshot.toObjects()
        } catch (e: FirebaseFirestoreException) {
            throw Exception(e.toString())
        }
    }

    private suspend inline fun <reified T : Any> querySnapshotHandling(
        reference: Query
    ): List<T> {
        try {
            val snapshot = reference.get().await()

            snapshot.documents.forEach {
                Timber.d("Data=${it.data?.keys}")
            }

            return snapshot.toObjects()
        } catch (e: FirebaseFirestoreException) {
            throw Exception(e.toString())
        }
    }

    private suspend inline fun <reified T> documentSnapshotHandling(
        ref: DocumentReference
    ): T {
        try {
            val snapshot = ref.get().await()
            return snapshot.toObject<T>()!!
        } catch (e: FirebaseFirestoreException) {
            throw Exception(e.toString())
        }
    }

    companion object {
        private const val PLANT_COLLECTION = "plants"
        private const val SAVE_DETECT_TRACE = "saveTask"
        private const val DETECT_COLLECTION = "detections"
    }
}
