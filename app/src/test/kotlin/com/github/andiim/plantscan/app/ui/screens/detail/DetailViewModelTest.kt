package com.github.andiim.plantscan.app.ui.screens.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.data.utils.MainDispatcherRule
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {
  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: DetailViewModel
  @Mock private lateinit var useCase: PlantUseCase
  @Mock private lateinit var log: LogService

  @Before
  fun setUp() {
    viewModel = DetailViewModel(useCase, log)
  }

  @Test
  fun `retrieveSaveData sets isSaved correctly`() = runTest {
    val plantId = "123"
    val isSaved = true

    // Mock pemanggilan isAddedToGarden pada useCase
    `when`(useCase.isAddedToGarden(plantId)).thenReturn(flowOf(isSaved))

    // Panggil fungsi retrieveSaveData
    viewModel.retrieveSaveData(plantId)

    // Verifikasi bahwa isSaved di-update dengan benar
    assertEquals(isSaved, viewModel.isSaved.value)
  }

  @Test
  fun `getDetail sets uiState correctly`() = runTest {
    val plantId = "123"
    val fakePlant = Plant(id = plantId, name = "Plant A")
    val fakeResource = Resource.Success(fakePlant)

    // Mock pemanggilan getPlantDetail pada useCase
    `when`(useCase.getPlantDetail(plantId)).thenReturn(flowOf(fakeResource))

    // Panggil fungsi getDetail
    viewModel.getDetail(plantId)

    // Verifikasi bahwa uiState di-update dengan benar
    assertEquals(fakeResource, viewModel.uiState.value)
  }

  @Test
  fun `setPlantToGarden calls addPlantToGarden and updates isSaved`() = runTest {
    val fakePlant = Plant(id = "123", name = "Plant A")

    // Panggil fungsi setPlantToGarden
    viewModel.setPlantToGarden(fakePlant)

    // Verifikasi bahwa addPlantToGarden pada useCase dipanggil
    verify(useCase).addPlantToGarden(fakePlant)

    // Verifikasi bahwa retrieveSaveData dipanggil dengan id tanaman
    verify(useCase).isAddedToGarden(fakePlant.id)
  }

  @Test
  fun `removePlantFromGarden calls removePlantFromGarden and updates isSaved`() = runTest {
    val fakePlant = Plant(id = "123", name = "Plant A")

    // Panggil fungsi removePlantFromGarden
    viewModel.removePlantFromGarden(fakePlant)

    // Verifikasi bahwa removePlantFromGarden pada useCase dipanggil
    verify(useCase).removePlantFromGarden(fakePlant)

    // Verifikasi bahwa retrieveSaveData dipanggil dengan id tanaman
    verify(useCase).isAddedToGarden(fakePlant.id)
  }
}
