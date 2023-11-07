package com.github.andiim.plantscan.core.auth

import com.github.andiim.plantscan.core.auth.model.User
import kotlinx.coroutines.flow.Flow

interface AuthHelper {
    val currentUser: Flow<User>
    suspend fun authenticate(email: String, password: String): Flow<Unit>
    suspend fun sendRecoveryEmail(email: String): Flow<Unit>
    fun createAnonymousAccount(): Flow<Unit>
    fun linkAccount(email: String, password: String): Flow<Unit>
    fun deleteAccount(): Flow<Unit>
    fun signOut(): Flow<Unit>
}
