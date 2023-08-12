package com.github.andiim.plantscan.app.ui.screens.home.myGarden

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.andiim.plantscan.app.core.data.source.firebase.FakeLogService
import com.github.andiim.plantscan.app.core.data.utils.DataDummy
import com.github.andiim.plantscan.app.core.data.utils.MainDispatcherRule
import com.github.andiim.plantscan.app.core.domain.usecase.FakeUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyGardenViewModelTest {
  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: MyGardenViewModel
  private lateinit var useCase: PlantUseCase
  private lateinit var log: LogService

  @Before
  fun setUp() {
    useCase = FakeUseCase()
    log = FakeLogService()
    viewModel = MyGardenViewModel(useCase, log)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `myGarden should update fetchedData`() = runTest {
    val expected = DataDummy.PLANTS

    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.myPlant.collect() }
    val actual = viewModel.myPlant.value
    Assert.assertEquals(expected, actual)
  }
}
