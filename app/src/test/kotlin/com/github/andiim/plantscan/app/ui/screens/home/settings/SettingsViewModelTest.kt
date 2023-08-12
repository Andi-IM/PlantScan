package com.github.andiim.plantscan.app.ui.screens.home.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.andiim.plantscan.app.core.data.utils.MainDispatcherRule
import com.github.andiim.plantscan.app.core.domain.model.User
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.AccountService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest {
  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: SettingsViewModel
  @Mock private lateinit var service: AccountService
  @Mock private lateinit var log: LogService

  @Before
  fun setUp() {
    viewModel = SettingsViewModel(service, log)
  }

  @Test
  fun `uiState reflects user's anonymous status`() = runTest {
    // Persiapan data palsu
    val mockUser = User(isAnonymous = true)
    val expectedUiState = SettingsUiState(isAnonymousAccount = true)
    val userFlow = MutableStateFlow(mockUser)

    // Mock pemanggilan currentUser pada accountService
    `when`(service.currentUser).thenReturn(userFlow)

    // Ambil data dari uiState dan konversi menjadi SettingsUiState
    val uiState: SettingsUiState = viewModel.uiState.first()

    // Verifikasi bahwa uiState sesuai dengan yang diharapkan
    assertEquals(expectedUiState, uiState)
  }
}
