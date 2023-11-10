package com.github.andiim.plantscan.core.data.repository.fake

import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.data.repository.PlantRepository
import com.github.andiim.plantscan.core.firestore.fake.FakePsFirebaseDataSource
import com.github.andiim.plantscan.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.core.model.data.Plant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Fake implementation of the [PlantRepository].
 */
class FakePlantRepository @Inject constructor(
    private val dataSource: FakePsFirebaseDataSource,
) : PlantRepository {
    override fun getPlants(): Flow<List<Plant>> =
        flow {
            dataSource.getPlants().map(PlantDocument::asExternalModel)
        }

    override fun getPlantById(id: String): Flow<Plant> {
        return getPlants().map { it.first { plant -> plant.id == id } }
    }
}
