package com.github.andiim.plantscan.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.andiim.plantscan.core.database.dao.RecentSearchQueryDao
import com.github.andiim.plantscan.core.database.model.RecentSearchQueryEntity
import com.github.andiim.plantscan.core.database.util.InstantConverter

@Database(
    entities = [
        RecentSearchQueryEntity::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
)
abstract class PsDatabase : RoomDatabase() {
    abstract fun recentSearchQueryDao(): RecentSearchQueryDao
}
