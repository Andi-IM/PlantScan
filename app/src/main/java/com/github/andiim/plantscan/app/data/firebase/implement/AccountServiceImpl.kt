package com.github.andiim.plantscan.app.data.firebase.implement

import com.github.andiim.plantscan.app.data.firebase.AccountService
import com.github.andiim.plantscan.app.data.model.User
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AccountServiceImpl @Inject constructor() : AccountService {
    override val currentUserId: String
        get() = "1"
    override val hasUser: Boolean
        get() = true
    override val currentUser: Flow<User>
        get() = flowOf(User(id = "1", isAnonymous = true))

    override suspend fun authenticate(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun sendRecoveryEmail(email: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createAnonymousAccount() {
        TODO("Not yet implemented")
    }

    override suspend fun linkAccount(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount() {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }
}