package com.github.andiim.plantscan.feature.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.github.andiim.plantscan.app.core.data.source.firebase.FakeLogService
import com.github.andiim.plantscan.app.utils.DataDummy
import com.github.andiim.plantscan.app.core.data.utils.MainDispatcherRule
import com.github.andiim.plantscan.app.core.domain.usecase.FakeUseCase
import com.github.andiim.plantscan.core.crashlytics.LogService
import com.github.andiim.plantscan.domain.PlantUseCase
import com.github.andiim.plantscan.domain.firebase_services.LogService
import com.github.andiim.plantscan.testing.util.MainDispatcherRule
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FindPlantViewModelTest {
  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: FindPlantViewModel
  private lateinit var useCase: PlantUseCase
  private lateinit var log: LogService

  @Before
  fun setUp() {
    useCase = FakeUseCase()
    log = FakeLogService()
    viewModel = FindPlantViewModel(useCase, log)
  }

  @Test
  fun `onQueryChange should update query value`() = runTest {
    viewModel.onQueryChange("testQuery")
    delay(1100)
    assertEquals("testQuery", viewModel.query.value)
  }

  @Test
  fun `searchPlant should update fetchedData`() = runTest {
    val query = "name@2"

    val expected =
        DataDummy.PLANTS.filter { plant -> plant.name == query || plant.species == query }

    viewModel.onQueryChange(query)

    // Delay sebentar untuk memastikan debounce terpenuhi
    delay(1100)

    val items: Flow<PagingData<com.github.andiim.plantscan.model.data.Plant>> = viewModel.items
    val snapshot = items.asSnapshot()

    assertEquals(expected, snapshot)
  }

  @Test
  fun `searchPlant should update when go search fetchedData`() = runTest {
    val query = "name@2"

    val expected =
        DataDummy.PLANTS.filter { plant -> plant.name == query || plant.species == query }

    viewModel.onSearch(query)

    // Delay sebentar untuk memastikan debounce terpenuhi
    delay(1100)

    val items: Flow<PagingData<com.github.andiim.plantscan.model.data.Plant>> = viewModel.items
    val snapshot = items.asSnapshot()

    assertEquals(expected, snapshot)
  }

  @Test
  fun `searchPlant should update fetchedData to empty`() = runTest {
    val query = ""
    viewModel.onQueryChange(query)
    assertTrue(viewModel.query.value.isEmpty())

    val items: Flow<PagingData<com.github.andiim.plantscan.model.data.Plant>> = viewModel.items
    val snapshot = items.asSnapshot()
    assertTrue(snapshot.isEmpty())
  }
}
