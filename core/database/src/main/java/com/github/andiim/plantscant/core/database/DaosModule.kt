package com.github.andiim.plantscant.core.database

import com.github.andiim.plantscant.core.database.dao.PlantDao
import com.github.andiim.plantscant.core.database.dao.RecentSearchQueryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
  @Provides
  @Singleton
  fun provideDao(
      database: PlantDatabase,
  ): PlantDao = database.plantDao()

  @Provides
  fun providesRecentSearchQueryDao(
      database: PlantDatabase,
  ): RecentSearchQueryDao = database.recentSearchQueryDao()
}
