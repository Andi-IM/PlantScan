package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.data.util.NetworkBoundResource
import com.github.andiim.plantscan.core.database.dao.PlantDao
import com.github.andiim.plantscan.core.database.model.PlantAndImages
import com.github.andiim.plantscan.core.database.model.PlantEntity
import com.github.andiim.plantscan.core.database.model.PlantImageEntity
import com.github.andiim.plantscan.core.database.model.asExternalModel
import com.github.andiim.plantscan.core.firestore.FirebaseDataSource
import com.github.andiim.plantscan.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.core.model.data.Plant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface PlantRepository {
    /**
     * Gets the available plants as a stream.
     */
    suspend fun getPlants(): Flow<List<Plant>>

    /**
     * Gets data for a specific plant.
     */
    suspend fun getPlantById(id: String): Flow<Plant>
}

class DefaultPlantRepository @Inject constructor(
    private val firebase: FirebaseDataSource,
    private val plantDao: PlantDao,
) : PlantRepository {
    override suspend fun getPlants(): Flow<List<Plant>> =
        object : NetworkBoundResource<List<Plant>, List<PlantDocument>>() {
            override fun loadFromDB(): Flow<List<Plant>> {
                return plantDao.getFlowPlantWithImage().map { list ->
                    list.map(PlantAndImages::asExternalModel)
                }
            }

            override suspend fun createCall(): Flow<List<PlantDocument>> =
                firebase.getPlants()

            override suspend fun saveCallResult(data: List<PlantDocument>) {
                val plants = data.map(PlantDocument::asExternalModel)
                val plantData = plants.map { plant ->
                    PlantEntity(
                        id = plant.id,
                        name = plant.name,
                        species = plant.species,
                        description = plant.description,
                        thumbnail = plant.thumbnail,
                        commonName = plant.commonName,
                    )
                }
                plantDao.insertPlant(plantData)
                plants.forEach { plant ->
                    val imageData = plant.images.map {
                        PlantImageEntity(
                            plantImageId = it.id,
                            plantRefId = plant.id,
                            url = it.url,
                            attribution = it.attribution,
                            description = it.description,
                        )
                    }
                    plantDao.insertPlantImage(imageData)
                }
            }

            override fun shouldFetch(data: List<Plant>?): Boolean =
                data.isNullOrEmpty()
        }.asFlow()

    override suspend fun getPlantById(id: String): Flow<Plant> =
        firebase.getPlantById(id).map(PlantDocument::asExternalModel)
}
