package com.github.andiim.plantscan.app.core.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.github.andiim.plantscan.app.core.data.source.firebase.RemotePlantSource
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.DbResponse
import com.github.andiim.plantscan.app.core.data.utils.DataDummy
import com.github.andiim.plantscan.app.core.data.utils.MainDispatcherRule
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PlantRepositoryTest {
  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  @Mock private lateinit var apiService: RemotePlantSource
  private lateinit var repository: PlantRepository

  @Before
  fun setUp() {
    repository = PlantRepositoryImpl(apiService)
  }

  @Test
  fun `when getPlants Should Not Null`() = runTest {
//    val paging = PagingData.from(Plant.Companion.mapFromResponse(DataDummy.PLANTS))
//    val expected = DbResponse.Success(DataDummy.PLANTS)
//
//    `when`(apiService.getAllPlant()).thenReturn(expected)
//
//    val resultFlow = repository.getAllPlant()
//    resultFlow.test {
//      Assert.assertEquals(paging, awaitItem())
//      cancelAndIgnoreRemainingEvents()
//    }
  }
}
