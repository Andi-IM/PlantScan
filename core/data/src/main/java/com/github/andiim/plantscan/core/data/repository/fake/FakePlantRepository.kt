package com.github.andiim.plantscan.core.data.repository.fake

import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.data.repository.PlantRepository
import com.github.andiim.plantscan.core.firestore.fake.FakeFirebaseDataSource
import com.github.andiim.plantscan.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.core.model.data.Plant
import com.github.andiim.plantscan.core.network.AppDispatchers.IO
import com.github.andiim.plantscan.core.network.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Fake implementation of the [PlantRepository].
 */
class FakePlantRepository @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val dataSource: FakeFirebaseDataSource,
) : PlantRepository {
    override suspend fun getPlants(): Flow<List<Plant>> =
        dataSource.getPlants().map { it.map(PlantDocument::asExternalModel) }

    override suspend fun getPlantById(id: String): Flow<Plant> {
        return getPlants().map { it.first { plant -> plant.id == id } }
    }
}
