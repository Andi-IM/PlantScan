package com.github.andiim.plantscan.app.core.di

import android.content.Context
import androidx.room.Room
import com.github.andiim.plantscan.app.core.data.source.local.room.PlantDao
import com.github.andiim.plantscan.app.core.data.source.local.room.PlantDatabase
import com.github.andiim.plantscan.app.core.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
  @Singleton
  @Provides
  fun provideDatabase(@ApplicationContext context: Context): PlantDatabase {
    // val passphrase: ByteArray = SQLiteDatabase.getBytes("moviecatalog".toCharArray())
    // val factory = SupportFactory(passphrase)
    return Room.databaseBuilder(
            context.applicationContext, PlantDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        // .openHelperFactory(factory)
        .build()
  }

  @Provides fun provideDao(database: PlantDatabase): PlantDao = database.plantDao()
}
