package com.github.andiim.plantscant.core.database

import android.content.Context
import androidx.room.Room
import com.github.andiim.plantscant.core.database.dao.PlantDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
  @Singleton
  @Provides
  fun provideDatabase(
      @ApplicationContext context: Context,
  ): PlantDatabase {
    // val passphrase: ByteArray = SQLiteDatabase.getBytes("moviecatalog".toCharArray())
    // val factory = SupportFactory(passphrase)
    return Room.databaseBuilder(
            context.applicationContext,
            PlantDatabase::class.java,
            "plant_database",
        )
        .fallbackToDestructiveMigration()
        // .openHelperFactory(factory)
        .build()
  }
}
