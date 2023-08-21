package com.github.andiim.plantscan.feature.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.andiim.plantscan.app.core.data.utils.MainDispatcherRule
import com.github.andiim.plantscan.domain.firebase_services.AccountService
import com.github.andiim.plantscan.domain.firebase_services.ConfigurationService
import com.github.andiim.plantscan.domain.firebase_services.LogService
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {
  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: SplashViewModel
  @Mock private lateinit var config: com.github.andiim.plantscan.domain.firebase_services.ConfigurationService
  @Mock private lateinit var account: com.github.andiim.plantscan.domain.firebase_services.AccountService
  @Mock private lateinit var log: com.github.andiim.plantscan.domain.firebase_services.LogService

  @Before
  fun setUp() {
    viewModel = SplashViewModel(config, account, log)
  }

  @Test
  fun `test showError initialized to false`() {
    assertFalse(viewModel.showError.value)
  }

  @Test
  fun `test onAppStart when user is logged in`() {
    `when`(account.hasUser).thenReturn(true)
    val mockOpenAndPopUp: (String, String) -> Unit = { _, _ -> }

    viewModel.onAppStart(mockOpenAndPopUp)

    verify(account).hasUser
    assertFalse(viewModel.showError.value)
  }

  @Test
  fun `test onAppStart when user is not logged in`() {
    // Setup
    `when`(account.hasUser).thenReturn(false)
    val mockOpenAndPopup: (String, String) -> Unit = { _, _ -> }

    // Action
    viewModel.onAppStart(mockOpenAndPopup)

    // Verification
    verify(account).hasUser
    assertFalse(viewModel.showError.value)
  }

  @Test
  fun `test onAppStart when get some error`() = runTest {
    // Setup
    `when`(account.hasUser).thenReturn(false)
    `when`(account.createAnonymousAccount()).thenThrow(RuntimeException("An error occurred"))
    val mockOpenAndPopup: (String, String) -> Unit = { _, _ -> }

    // Action
    viewModel.onAppStart(mockOpenAndPopup)

    // Verification
    assertTrue(viewModel.showError.value)
  }
}
