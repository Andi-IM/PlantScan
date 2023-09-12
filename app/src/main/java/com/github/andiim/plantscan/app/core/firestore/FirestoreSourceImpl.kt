package com.github.andiim.plantscan.app.core.firestore

import android.graphics.Bitmap
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.data.source.network.Dispatcher
import com.github.andiim.plantscan.app.core.data.source.network.PsDispatchers.IO
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.trace
import com.github.andiim.plantscan.app.core.firestore.model.DetectionHistoryDocument
import com.github.andiim.plantscan.app.core.firestore.model.ImageContent
import com.github.andiim.plantscan.app.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.app.core.firestore.model.SuggestionDocument
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class FirestoreSourceImpl @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
) :
    FirestoreSource {

    override suspend fun getPlants(limit: Long, query: String): List<PlantDocument> =
        querySnapshotHandling(
            db.collection(PLANT_COLLECTION).whereGreaterThanOrEqualTo(NAME_FIELD, query)
                .limit(limit)
        )

    override suspend fun getPlantById(id: String): PlantDocument =
        documentSnapshotHandling(db.collection(PLANT_COLLECTION).document(id))

    override suspend fun recordDetection(detection: DetectionHistoryDocument): String =
        trace(SAVE_DETECT_TRACE) {
            db.collection(DETECT_COLLECTION).add(detection).await().id
        }

    override suspend fun getDetectionsList(userId: String): List<DetectionHistoryDocument> =
        querySnapshotHandling(db.collection(DETECT_COLLECTION).whereEqualTo(USER_ID_FIELD, userId))

    override suspend fun sendASuggestion(suggestion: SuggestionDocument): String =
        trace(SAVE_SUGGESTION_TRACE) {
            db.collection(SUGGESTION_COLLECTION).add(suggestion).await().id
        }

    override fun uploadSuggestionImage(content: ImageContent): Flow<String> = flow {
        val storageRef =
            storage.reference.child("$SUGGESTION_COLLECTION/${content.ref}.jpg")

        val byteArray = ByteArrayOutputStream()
        val data =
            byteArray.also {
                content.image.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }.toByteArray()

        val downloadUrl = storageRef.putBytes(data)
            .await()
            .storage
            .downloadUrl
            .await()
            .toString()

        emit(downloadUrl)
    }.flowOn(ioDispatcher)


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
        private const val SAVE_DETECT_TRACE = "saveDetect"
        private const val SUGGESTION_COLLECTION = "suggestions"
        private const val SAVE_SUGGESTION_TRACE = "saveSuggestion"
        private const val DETECT_COLLECTION = "detections"
        private const val USER_ID_FIELD = "userId"
        private const val NAME_FIELD = "name"
    }
}
