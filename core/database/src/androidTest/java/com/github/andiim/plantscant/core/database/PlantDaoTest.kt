package com.github.andiim.plantscant.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.github.andiim.plantscant.core.database.dao.PlantDao
import com.github.andiim.plantscant.core.database.model.PlantEntity
import org.junit.Before
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PlantDaoTest {
  private lateinit var dao: PlantDao
  private lateinit var db: PlantDatabase
  @Before
  fun createDb() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db =
        Room.inMemoryDatabaseBuilder(
                context,
                PlantDatabase::class.java,
            )
            .build()
    dao = db.plantDao()
  }
  @Test
  fun useAppContext() {
    val plantResourceEntities =
        listOf(
            testPlantEntity(name = "1"),
            testPlantEntity(id = "2", name = "2"),
            testPlantEntity(id = "3", name = "3"))
  }
}

private fun testPlantEntity(
    id: String = "0",
    name: String,
) =
    PlantEntity(
        id = id,
        name = name,
    )
