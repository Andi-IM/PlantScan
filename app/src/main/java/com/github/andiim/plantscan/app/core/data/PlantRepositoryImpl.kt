package com.github.andiim.plantscan.app.core.data

import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.BuildConfig
import com.github.andiim.plantscan.app.core.data.mediator.PlantPagingSource
import com.github.andiim.plantscan.app.core.data.source.network.NetworkDataSource
import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.github.andiim.plantscan.app.core.domain.model.Image
import com.github.andiim.plantscan.app.core.domain.model.ObjectDetection
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.Suggestion
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import com.github.andiim.plantscan.app.core.firestore.FirestoreSource
import com.github.andiim.plantscan.app.core.firestore.model.DetectionHistoryDocument
import com.github.andiim.plantscan.app.core.firestore.model.ImageContent
import com.github.andiim.plantscan.app.core.firestore.model.SuggestionDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlantRepositoryImpl
@Inject
constructor(
    private val network: NetworkDataSource,
    private val remote: FirestoreSource
) : PlantRepository {
    override fun getPlants(query: String): PagingSource<Int, Plant> {
        return PlantPagingSource(remote).apply { setQuery(query) }
    }

    override fun getPlantDetail(id: String): Flow<Resource<Plant>> = flow {
        try {
            val response = remote.getPlantById(id)
            emit(Resource.Success(response.toModel()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.orEmpty()))
        }
    }

    override fun getPlantBySpecies(species: String): Flow<Resource<Plant>> = flow {
        try {
            val response = remote.getPlantBySpecies(species)
            emit(Resource.Success(response.toModel()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.orEmpty()))
        }
    }

    override fun recordDetection(detection: DetectionHistory): Flow<String> = flow {
        val result = remote.recordDetection(DetectionHistoryDocument.fromModel(detection))
        emit(result)
    }

    override fun detect(base64ImageData: String, confidence: Int): Flow<Resource<ObjectDetection>> =
        flow {
            try {
                val response = network.detect(base64ImageData, confidence)
                emit(Resource.Success(response.toModel()))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage.orEmpty()))
            }
        }

    override fun getDetectionsList(userId: String): Flow<Resource<List<DetectionHistory>>> = flow {
        try {
            val response = remote.getDetectionsList(userId)
            emit(Resource.Success(response.map { it.toModel() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.orEmpty()))
        }
    }

    override fun sendSuggestion(suggestion: Suggestion): Flow<String> = flow {
        try {
            var data = suggestion
            if (suggestion.image.isNotEmpty()) {
                val downloadUrls = mutableListOf<String>()
                suggestion.image.forEachIndexed { index, bitmap ->
                    remote.uploadSuggestionImage(
                        ImageContent(
                            bitmap,
                            "${suggestion.userId}_${Clock.System.now()}/${suggestion.userId}_$index"
                        )
                    ).collectLatest {
                        downloadUrls.add(it)
                    }
                }

                data =
                    suggestion.copy(imageUrl = downloadUrls.map { Image(it, Clock.System.now()) })
            }

            val id = remote.sendASuggestion(SuggestionDocument.fromModel(data))
            emit(id)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Timber.d("Error $e")
            }
            throw Exception(e)
        }
    }
}

