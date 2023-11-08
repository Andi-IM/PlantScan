package com.github.andiim.plantscan.core.auth

import com.github.andiim.plantscan.core.auth.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/**
 * An implementation of [AuthHelper] that passes an user to app. No data just sent to a backend.
 */
@Singleton
class StubAuthHelper @Inject constructor() : AuthHelper {
    private val _currentUser = MutableStateFlow(User())
    override val currentUser: Flow<User> = _currentUser.asStateFlow()
    override fun authenticate(email: String, password: String): Flow<Unit> = flow {
        _currentUser.value = _currentUser.value.copy(isAnonymous = false)
    }

    override fun sendRecoveryEmail(email: String): Flow<Unit> = flowOf()
    override fun createAnonymousAccount(): Flow<Unit> = flow {
        _currentUser.value = _currentUser.value.copy(id = "demo", isAnonymous = true)
    }

    override fun linkAccount(email: String, password: String): Flow<Unit> = flow {
        _currentUser.value = _currentUser.value.copy(isAnonymous = false)
    }

    override fun deleteAccount(): Flow<Unit> = flow {
        _currentUser.value = _currentUser.value.copy(isAnonymous = true)
    }

    override fun signOut(): Flow<Unit> = flow {
        _currentUser.value = _currentUser.value.copy(isAnonymous = true)
    }
}
