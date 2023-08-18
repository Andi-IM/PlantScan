package com.github.andiim.plantscan.app.ui.screens.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.github.andiim.plantscan.app.utils.DataDummy
import com.github.andiim.plantscan.app.core.data.utils.MainDispatcherRule
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PlantListViewModelTest {
  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: PlantListViewModel
  @Mock private lateinit var useCase: PlantUseCase
  @Mock private lateinit var log: LogService

  @Before
  fun setUp() {
    viewModel = PlantListViewModel(useCase, log)
  }

  @Test
  fun `fetchedData contains expected plant list`() = runTest {
    val flow = flow { emit(PagingData.from(DataDummy.PLANTS)) }

    // Mock pemanggilan getAllPlant pada plantRepository
    `when`(useCase.getAllPlant()).thenReturn(flow)

    // Ambil data dari fetchedData dan konversi menjadi list
    val fetchedDataList = viewModel.fetchData()

    // Verifikasi bahwa data di dalam fetchedData sesuai dengan yang diharapkan
    assertEquals(flow, fetchedDataList)

    // Verifikasi bahwa pemanggilan getAllPlant pada plantRepository terjadi
    verify(useCase).getAllPlant()
  }
}
