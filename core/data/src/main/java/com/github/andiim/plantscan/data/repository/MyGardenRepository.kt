package com.github.andiim.plantscan.data.repository

import com.github.andiim.plantscan.core.common.network.Dispatcher
import com.github.andiim.plantscan.core.common.network.PlantScantDispatchers
import kotlinx.coroutines.flow.Flow
import com.github.andiim.plantscan.data.model.PlantResource
import com.github.andiim.plantscan.model.data.Plant
import com.github.andiim.plantscant.core.database.dao.PlantDao
import com.github.andiim.plantscant.core.database.model.PlantEntity
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Data layer implementation for [PlantResource]
 */
interface MyGardenRepository {
    /**
     * Returns available plant as a stream.
     */
    val plants: Flow<List<com.github.andiim.plantscan.model.data.Plant>>
    fun isAddedToGarden(id: String): Flow<Boolean>
    suspend fun addPlantToGarden(plant: com.github.andiim.plantscan.model.data.Plant)
    suspend fun removePlantFromGarden(plant: com.github.andiim.plantscan.model.data.Plant)
}

class DefaultMyGardenRepository
@Inject
constructor(
    private val plantDao: PlantDao,
    @Dispatcher(PlantScantDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : MyGardenRepository {
    override val plants: Flow<List<Plant>> =
        plantDao.gardenList().map { it.map(PlantEntity::asExternalModel) }

    override fun isAddedToGarden(id: String): Flow<Boolean> = flow {
        val data = plantDao.getPlantById(id)
        emit(data.first().isNotEmpty())
    }

    override suspend fun addPlantToGarden(plant: Plant) {
        withContext(ioDispatcher) {
            val entity = PlantEntity(plant)
            plantDao.addPlant(entity)
        }
    }

    override suspend fun removePlantFromGarden(plant: Plant) {
        withContext(ioDispatcher) {
            val entity = PlantEntity(plant)
            plantDao.removePlant(entity)
        }
    }
}