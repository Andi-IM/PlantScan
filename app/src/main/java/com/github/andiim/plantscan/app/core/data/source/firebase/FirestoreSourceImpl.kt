package com.github.andiim.plantscan.app.core.data.source.firebase

import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.DetectionDocument
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PlantResponse
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.FirestoreSource
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.trace
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class FirestoreSourceImpl @Inject constructor(private val db: FirebaseFirestore) :
    FirestoreSource {

    override suspend fun getPlants(query: String, limit: Long): List<PlantResponse> =
        querySnapshotHandling(
            db.collection(PLANT_COLLECTION).whereGreaterThanOrEqualTo("name", query).limit(limit)
        )

    override suspend fun getPlantById(id: String): PlantResponse =
        documentSnapshotHandling(db.collection(PLANT_COLLECTION).document(id))

    override suspend fun recordDetection(detection: DetectionDocument): String =
        trace(SAVE_DETECT_TRACE) {
            db.collection(DETECT_COLLECTION).add(detection).await().id
        }

    override suspend fun getDetectionsList(): List<DetectionDocument> =
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
