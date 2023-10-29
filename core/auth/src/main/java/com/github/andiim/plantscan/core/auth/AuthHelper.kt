package com.github.andiim.plantscan.core.auth

import com.github.andiim.plantscan.core.auth.model.User
import kotlinx.coroutines.flow.Flow

interface AuthHelper {
    val currentUserId: String
    val hasUser: Boolean
    val isAnonymous: Boolean
    val currentUser: Flow<User>

    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAnonymousAccount()
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}
