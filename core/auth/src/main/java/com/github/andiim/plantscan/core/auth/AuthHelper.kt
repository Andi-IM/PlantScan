package com.github.andiim.plantscan.core.auth

import com.github.andiim.plantscan.core.auth.model.User
import kotlinx.coroutines.flow.Flow

interface AuthHelper {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<User>
    fun authenticate(email: String, password: String): Flow<Unit>
    fun sendRecoveryEmail(email: String): Flow<Unit>
    fun createAnonymousAccount(): Flow<Unit>
    fun linkAccount(email: String, password: String): Flow<Unit>
    fun deleteAccount(): Flow<Unit>
    fun signOut(): Flow<Unit>
}
