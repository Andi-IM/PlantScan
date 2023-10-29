package com.github.andiim.plantscan.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.andiim.plantscan.core.database.dao.PlantDao
import com.github.andiim.plantscan.core.database.dao.PlantFtsDao
import com.github.andiim.plantscan.core.database.dao.RecentSearchQueryDao
import com.github.andiim.plantscan.core.database.model.PlantEntity
import com.github.andiim.plantscan.core.database.model.PlantFtsEntity
import com.github.andiim.plantscan.core.database.model.PlantImageEntity
import com.github.andiim.plantscan.core.database.model.RecentSearchQueryEntity
import com.github.andiim.plantscan.core.database.util.InstantConverter
import com.github.andiim.plantscan.core.database.util.ListConverter

@Database(
    entities = [
        RecentSearchQueryEntity::class,
        PlantEntity::class,
        PlantImageEntity::class,
        PlantFtsEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
    ListConverter::class,
)
abstract class PsDatabase : RoomDatabase() {
    abstract fun recentSearchQueryDao(): RecentSearchQueryDao
    abstract fun plantDao(): PlantDao
    abstract fun plantFtsDao(): PlantFtsDao
}
