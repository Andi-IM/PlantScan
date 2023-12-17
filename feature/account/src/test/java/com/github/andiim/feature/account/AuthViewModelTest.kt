package com.github.andiim.feature.account

import com.github.andiim.plantscan.core.auth.StubAuthHelper
import com.github.andiim.plantscan.core.domain.GetUserLoginInfoUseCase
import com.github.andiim.plantscan.core.testing.repository.TestUserDataRepository
import com.github.andiim.plantscan.core.testing.util.MainDispatcherRule
import com.github.andiim.plantscan.feature.account.AuthViewModel
import com.github.andiim.plantscan.feature.account.model.AuthEvent
import com.github.andiim.plantscan.feature.account.model.AuthState
import com.github.andiim.plantscan.feature.account.model.AuthStatus
import com.github.andiim.plantscan.feature.account.model.EventAction
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class AuthViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()
    private val userDataRepository = TestUserDataRepository()
    private val authHelper = StubAuthHelper()
    private lateinit var getUserLoginInfoUseCase: GetUserLoginInfoUseCase
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        getUserLoginInfoUseCase = GetUserLoginInfoUseCase(authHelper)
        viewModel = AuthViewModel(
            userDataRepository = userDataRepository,
            authHelper = authHelper,
            getUserLoginInfoUseCase = getUserLoginInfoUseCase,
        )
    }

    @Test
    fun initialize_data_must_be_empty() = runTest {
        val state = AuthState()
        val status = AuthStatus()
        assertEquals(
            status,
            viewModel.authStatus.value,
        )
        assertEquals(
            state,
            viewModel.formState.value,
        )
    }

    @Test
    fun email_must_change_when_onEmailChange_is_invoked() = runTest {
        viewModel.onEvent(AuthEvent.EmailChanged("newValue"))

        assertEquals(
            "newValue",
            viewModel.formState.value.email,
        )
    }

    @Test
    fun password_must_change_when_onPasswordChange_is_invoked() = runTest {
        viewModel.onEvent(AuthEvent.PasswordChanged("newValue"))
        assertEquals(
            "newValue",
            viewModel.formState.value.password,
        )
    }

    @Test
    fun repeat_password_must_change_when_onRepeatPasswordChange_is_invoked() = runTest {
        viewModel.onEvent(AuthEvent.RepeatPasswordChanged("newValue"))
        assertEquals(
            "newValue",
            viewModel.formState.value.repeatPassword,
        )
    }

    @Test
    fun onSuccess_is_return_as_invoke_when_onSignIn_is_clicked() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.authStatus.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.formState.collect() }
        viewModel.onEvent(AuthEvent.EmailChanged("newValue"))
        viewModel.onEvent(AuthEvent.PasswordChanged("newValue"))
        val collectJob3 = launch(UnconfinedTestDispatcher()) {
            viewModel.onEvent(
                AuthEvent.EventActionChanged(EventAction.SIGN_IN),
            )
        }

        viewModel.onEvent(AuthEvent.Submit)

        assertEquals(
            false,
            viewModel.authStatus.value.granted,
        )
        collectJob.cancel()
        collectJob2.cancel()
        collectJob3.cancel()
    }
}
