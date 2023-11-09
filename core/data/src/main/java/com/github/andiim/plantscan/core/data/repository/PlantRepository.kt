package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.database.dao.PlantDao
import com.github.andiim.plantscan.core.database.model.asExternalModel
import com.github.andiim.plantscan.core.firestore.FirebaseDataSource
import com.github.andiim.plantscan.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.core.model.data.Plant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface PlantRepository {
    /**
     * Gets the available plants as a stream.
     */
    fun getPlants(): Flow<List<Plant>>

    /**
     * Gets data for a specific plant.
     */
    fun getPlantById(id: String): Flow<Plant>
}

class DefaultPlantRepository @Inject constructor(
    private val firebase: FirebaseDataSource,
    private val plantDao: PlantDao,
) : PlantRepository {
    override fun getPlants(): Flow<List<Plant>> = flow {
        emit(firebase.getPlants().map(PlantDocument::asExternalModel))
    }

    override fun getPlantById(id: String): Flow<Plant> = flow {
        emit(firebase.getPlantById(id).asExternalModel())
    }
}