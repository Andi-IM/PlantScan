package com.github.andiim.plantscan.core.firestore.network

import com.github.andiim.plantscan.core.firestore.FirebaseDataSource
import com.github.andiim.plantscan.core.firestore.model.HistoryDocument
import com.github.andiim.plantscan.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.core.firestore.model.SuggestionDocument
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseNetwork @Inject constructor(
    private val db: FirebaseFirestore,
) : FirebaseDataSource {
    override suspend fun getPlants(): Flow<List<PlantDocument>> =
        flowOf(querySnapshotHandling(db.collection(PLANT_COLLECTION)))

    override suspend fun getPlantById(id: String): Flow<PlantDocument> =
        flowOf(documentSnapshotHandling(db.collection(PLANT_COLLECTION).document(id)))

    override suspend fun recordDetection(detection: HistoryDocument): String =
        db.collection(DETECT_COLLECTION).add(detection).await().id

    override suspend fun getDetectionHistories(id: String): List<HistoryDocument> =
        querySnapshotHandling(db.collection(DETECT_COLLECTION).whereEqualTo(USER_ID_FIELD, id))

    override suspend fun sendSuggestion(suggestionDocument: SuggestionDocument): String =
        db.collection(SUGGESTION_COLLECTION).add(suggestionDocument).await().id

    private suspend inline fun <reified T : Any> querySnapshotHandling(reference: Query): List<T> {
        val snapshot = reference.get().await()
        return snapshot.toObjects()
    }

    private suspend inline fun <reified T> documentSnapshotHandling(ref: DocumentReference): T {
        val snapshot = ref.get().await()
        return snapshot.toObject<T>()!!
    }

    companion object {
        private const val PLANT_COLLECTION = "plants"
        private const val SUGGESTION_COLLECTION = "suggestions"
        private const val DETECT_COLLECTION = "detections"
        private const val USER_ID_FIELD = "userId"
        private const val NAME_FIELD = "name"
    }
}
