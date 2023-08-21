package com.github.andiim.plantscant.core.database.dao

import android.database.sqlite.SQLiteException
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.andiim.plantscant.core.database.model.PlantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  @Throws(SQLiteException::class)
  fun addPlant(plant: PlantEntity)
  @Delete @Throws(SQLiteException::class) fun removePlant(plant: PlantEntity)
  @Query("SELECT * FROM plants") fun gardenList(): Flow<List<PlantEntity>>
  @Transaction
  @Query("SELECT * FROM plants WHERE id = :id")
  fun getPlantById(id: String): Flow<List<PlantEntity>>
}
