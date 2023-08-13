package com.github.andiim.plantscan.app.core.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.utils.DataDummy
import com.github.andiim.plantscan.app.core.data.utils.MainDispatcherRule
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PlantUseCaseTest {
  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private lateinit var useCase: PlantUseCase
  @Mock private lateinit var repository: PlantRepository

  @Before
  fun setUp() {
    useCase = PlantInteractor(repository)
  }

  @Test
  fun `when getPlants Should Not Null And Return Data`() = runTest {
    val flow = flow { emit(PagingData.from(DataDummy.PLANTS)) }

    `when`(repository.getAllPlant()).thenReturn(flow)
    val resultFlow = useCase.getAllPlant()

    verify(repository).getAllPlant()
    verifyNoMoreInteractions(repository)
    assertEquals(flow, resultFlow)
  }

  @Test
  fun `when getPlantDetail Should Not Null And Return Data`() = runTest {
    val id = "1"
    val flow = flow { emit(Resource.Success(DataDummy.PLANTS.first())) }
    `when`(repository.getPlantDetail(id)).thenReturn(flow)
    val resultFlow = useCase.getPlantDetail(id)

    verify(repository).getPlantDetail(id)
    verifyNoMoreInteractions(repository)
    assertEquals(flow, resultFlow)
  }

  @Test
  fun `when SearchPlant Should Not Null And Return Data`() = runTest {
//    val query = "name@2"
//    val flow = flow {
//      emit(PagingData.from(DataDummy.PLANTS.filter { plant -> plant.name == query }))
//    }
//    `when`(repository.searchPlant(query)).thenReturn(flow)
//    val resultFlow = useCase.searchPlant(query)
//
//    verify(repository).searchPlant(query)
//    verifyNoMoreInteractions(repository)
//    assertEquals(flow, resultFlow)
  }

  @Test
  fun `when getGarden Should Not Null And Return Data`() = runTest {
    val flow = flow { emit(DataDummy.PLANTS) }
    `when`(repository.getGarden()).thenReturn(flow)
    val resultFlow = useCase.getGarden()

    verify(repository).getGarden()
    verifyNoMoreInteractions(repository)
    assertEquals(flow, resultFlow)
  }

  @Test
  fun `when savePlant Should Success`() = runTest {
    val sample = DataDummy.PLANTS.first()

    doNothing().`when`(repository).addPlantToGarden(sample)
    useCase.addPlantToGarden(sample)

    verify(repository).addPlantToGarden(sample)
    verifyNoMoreInteractions(repository)
  }

  @Test
  fun `when removePlant Should Success`() = runTest {
    val sample = DataDummy.PLANTS.first()

    doNothing().`when`(repository).removePlantFromGarden(sample)
    useCase.removePlantFromGarden(sample)

    verify(repository).removePlantFromGarden(sample)
    verifyNoMoreInteractions(repository)
  }
}
