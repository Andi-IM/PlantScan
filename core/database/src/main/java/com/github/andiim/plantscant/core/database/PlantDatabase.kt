package com.github.andiim.plantscant.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.andiim.plantscant.core.database.dao.PlantDao
import com.github.andiim.plantscant.core.database.dao.RecentSearchQueryDao
import com.github.andiim.plantscant.core.database.model.PlantEntity
import com.github.andiim.plantscant.core.database.model.RecentSearchQueryEntity
import com.github.andiim.plantscant.core.database.util.InstantConverter
import com.github.andiim.plantscant.core.database.util.ListStringConverter

@Database(
    entities =
        [
            PlantEntity::class,
            RecentSearchQueryEntity::class,
        ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    ListStringConverter::class,
    InstantConverter::class,
)
abstract class PlantDatabase : RoomDatabase() {
  abstract fun plantDao(): PlantDao
  abstract fun recentSearchQueryDao(): RecentSearchQueryDao
}
