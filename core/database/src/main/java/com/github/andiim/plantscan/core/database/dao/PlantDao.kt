package com.github.andiim.plantscan.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.github.andiim.plantscan.core.database.model.PlantAndImages
import com.github.andiim.plantscan.core.database.model.PlantEntity
import com.github.andiim.plantscan.core.database.model.PlantImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    /**
     * Inserts or updates [plants] into the db.
     */
    @Upsert
    fun insertPlant(plants: List<PlantEntity>)

    /**
     * Inserts or updates [plantImage] into the db.
     */
    @Upsert
    fun insertPlantImage(plantImage: List<PlantImageEntity>)

    @Transaction
    @Query(value = "SELECT * FROM plants")
    fun getFlowPlantWithImage(): Flow<List<PlantAndImages>>

    @Transaction
    @Query(value = "SELECT * FROM plants")
    fun getPlantWithImage(): List<PlantAndImages>

    @Transaction
    @Query(
        value = """
        SELECT * FROM plants
        WHERE id IN (:ids)
    """,
    )
    fun getPlantEntities(ids: Set<String>): Flow<List<PlantAndImages>>
}
