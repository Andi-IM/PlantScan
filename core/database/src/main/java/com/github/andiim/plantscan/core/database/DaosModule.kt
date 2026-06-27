package com.github.andiim.plantscan.core.database

import com.github.andiim.plantscan.core.database.dao.PlantDao
import com.github.andiim.plantscan.core.database.dao.PlantFtsDao
import com.github.andiim.plantscan.core.database.dao.RecentSearchQueryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesRecentSearchQueryDao(
        database: PsDatabase,
    ): RecentSearchQueryDao = database.recentSearchQueryDao()

    @Provides
    fun providesPlantDao(
        database: PsDatabase,
    ): PlantDao = database.plantDao()

    @Provides
    fun providesPlantFtsDao(
        database: PsDatabase,
    ): PlantFtsDao = database.plantFtsDao()
}
