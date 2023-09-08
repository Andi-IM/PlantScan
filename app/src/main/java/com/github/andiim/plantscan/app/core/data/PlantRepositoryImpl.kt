package com.github.andiim.plantscan.app.core.data

import android.graphics.Bitmap
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.core.data.mediator.PlantPagingSource
import com.github.andiim.plantscan.app.core.data.source.network.NetworkDataSource
import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.github.andiim.plantscan.app.core.domain.model.ObjectDetection
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.Suggestion
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import com.github.andiim.plantscan.app.core.firestore.FirestoreSource
import com.github.andiim.plantscan.app.core.firestore.model.DetectionHistoryDocument
import com.github.andiim.plantscan.app.core.firestore.model.ImageContent
import com.github.andiim.plantscan.app.core.firestore.model.SuggestionDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlantRepositoryImpl
@Inject
constructor(
    private val network: NetworkDataSource,
    private val remote: FirestoreSource,
) : PlantRepository {

    companion object {
        fun getDefaultPageConfig(): PagingConfig {
            return PagingConfig(
                pageSize = PlantPagingSource.NETWORK_PAGE_SIZE, enablePlaceholders = false
            )
        }
    }

    override fun getPlants(query: String): PagingSource<Int, Plant> =
        PlantPagingSource(remote = remote, query = query)

    override fun getPlantDetail(id: String): Flow<Resource<Plant>> = flow {
        try {
            val response = remote.getPlantById(id)
            emit(Resource.Success(response.toModel()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.orEmpty()))
        }
    }

    override fun recordDetection(detection: DetectionHistory): Flow<String> = flow {
        val result = remote.recordDetection(DetectionHistoryDocument.fromModel(detection))
        emit(result)
    }

    override fun detect(image: Bitmap): Flow<Resource<ObjectDetection>> = flow {
        try {
            val response = network.detect(image)
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

    override fun sendSuggestion(suggestion: Suggestion): Flow<Resource<String>> = flow {
        val id = remote.sendASuggestions(SuggestionDocument.fromModel(suggestion))
        if (suggestion.image.isNotEmpty()) {
            val suggestWithId = suggestion.copy(id = id)
            val downloadUrls = mutableListOf<String>()

            // Uploading an images
            suggestion.image.forEachIndexed { index, data ->
                emit(Resource.Loading("Uploading Image ${index + 1} of ${suggestion.image.size}"))
                val content = ImageContent(
                    data,
                    "${suggestWithId.id}/${suggestWithId.userId}_$index"
                )

                when (val status = remote.uploadSuggestionImage(content).first()) {
                    is Resource.Loading -> emit(Resource.Loading(status.progress))
                    is Resource.Error -> {
                        emit(Resource.Error(status.message))
                        return@flow
                    }

                    is Resource.Success -> {
                        val downloadUrl = status.data
                        downloadUrls.add(downloadUrl)
                    }
                }
            }

            val newSuggest = suggestWithId.copy(imageUrl = downloadUrls)
            remote.updateASuggestion(SuggestionDocument.fromModel(newSuggest))
            emit(Resource.Success("Success"))
        } else {
            emit(Resource.Success("Success"))
        }
    }
}
