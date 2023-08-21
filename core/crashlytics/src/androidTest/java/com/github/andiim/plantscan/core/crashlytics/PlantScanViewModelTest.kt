package com.github.andiim.plantscan.core.crashlytics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.andiim.plantscan.app.core.data.utils.MainDispatcherRule
import com.github.andiim.plantscan.domain.firebase_services.LogService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PlantScanViewModelTest {
  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: com.github.andiim.plantscan.core.common.base.PlantScanViewModel
  @Mock private lateinit var log: com.github.andiim.plantscan.domain.firebase_services.LogService

  @Before
  fun setUp() {
    viewModel = com.github.andiim.plantscan.core.common.base.PlantScanViewModel(log)
  }

  @Test
  fun `launchCatching should catch exception and call logService`() = runTest {
    // Simulasikan exception dalam blok suspending
    val exceptionMessage = "Test exception"
    val exception = RuntimeException(exceptionMessage)

    // Panggil fungsi yang diuji
    viewModel.launchCatching(block = { throw exception })

    // Verifikasi bahwa exception ditangkap
    verify(log).logNonFatalCrash(exception)
  }
}
