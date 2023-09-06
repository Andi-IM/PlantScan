package com.github.andiim.plantscan.app.core.data

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.core.data.mediator.PlantPagingSource
import com.github.andiim.plantscan.app.core.data.source.network.NetworkDataSource
import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.github.andiim.plantscan.app.core.domain.model.ObjectDetection
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import com.github.andiim.plantscan.app.core.firestore.FirestoreSource
import com.github.andiim.plantscan.app.core.firestore.model.DetectionHistoryDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
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

    override fun detect(image: File): Flow<Resource<ObjectDetection>> = flow {
        try {
            val response = network.detect(image)
            emit(Resource.Success(response.toModel()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.orEmpty()))
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

}
