package com.github.andiim.plantscan.app.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.andiim.plantscan.app.core.data.source.local.entity.PlantEntity

@Database(entities = [PlantEntity::class], version = 1, exportSchema = false)
@TypeConverters(ListStringConverter::class)
abstract class PlantDatabase : RoomDatabase() {
  abstract fun plantDao(): PlantDao
}

