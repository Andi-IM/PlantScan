package com.github.andiim.plantscan.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.andiim.plantscan.core.database.model.PlantFtsEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [String] access.
 */
@Dao
interface PlantFtsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<PlantFtsEntity>)

    @Query("SELECT plantId FROM plantFts WHERE plantFts MATCH :query")
    fun searchAllPlants(query: String): Flow<List<String>>

    @Query("SELECT count(*) FROM plantFts")
    fun getCount(): Flow<Int>
}
