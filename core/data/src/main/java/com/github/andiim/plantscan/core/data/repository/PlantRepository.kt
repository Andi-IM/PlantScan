package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.firestore.PsFirebaseDataSource
import com.github.andiim.plantscan.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.core.model.data.Plant
import com.github.andiim.plantscan.core.network.PsNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface PlantRepository {
    /**
     * Gets the available plants as a stream.
     */
    fun getPlants(): Flow<List<Plant>>

    fun searchPlants(query: String): Flow<List<Plant>>

    /**
     * Gets data for a specific plant.
     */
    fun getPlantById(id: String): Flow<Plant>

    /**
     * count data.
     */
    fun countPlant(): Flow<Int>
}

class DefaultPlantRepository @Inject constructor(
    private val firebase: PsFirebaseDataSource,
    private val network: PsNetworkDataSource,
) : PlantRepository {
    override fun getPlants(): Flow<List<Plant>> = flow {
        emit(firebase.getPlants().map(PlantDocument::asExternalModel))
    }

    override fun searchPlants(query: String): Flow<List<Plant>> = flow {
        val plants = network.search(query).asExternalModel().hits
        emit(plants)
    }

    override fun getPlantById(id: String): Flow<Plant> = flow {
        emit(firebase.getPlantById(id).asExternalModel())
    }

    override fun countPlant(): Flow<Int> = flow {
        emit(firebase.countPlants().toInt())
    }
}
