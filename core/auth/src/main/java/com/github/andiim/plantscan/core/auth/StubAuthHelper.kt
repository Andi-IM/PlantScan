package com.github.andiim.plantscan.core.auth

import com.github.andiim.plantscan.core.auth.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/**
 * An implementation of [AuthHelper] that passes an user to app. No data just sent to a backend.
 */
@Singleton
class StubAuthHelper @Inject constructor() : AuthHelper {
    override val currentUserId: String = "demo"
    override val hasUser: Boolean = true
    override val isAnonymous: Boolean = true
    override val currentUser: Flow<User> = flowOf(User(id = "demo", isAnonymous = true))
    override suspend fun authenticate(email: String, password: String) = Unit
    override suspend fun sendRecoveryEmail(email: String) = Unit
    override suspend fun createAnonymousAccount() = Unit
    override suspend fun linkAccount(email: String, password: String) = Unit
    override suspend fun deleteAccount() = Unit
    override suspend fun signOut() = Unit
}
