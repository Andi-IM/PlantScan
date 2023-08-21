package com.github.andiim.plantscan.data.repository

import com.github.andiim.plantscan.core.firestore.auth.AccountService
import com.github.andiim.plantscan.model.data.User
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<User>

    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAnonymousAccount()
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}

class AccountRepositoryImpl @Inject constructor(private val accountService: AccountService) :
    AccountRepository {
    override val currentUserId: String = accountService.currentUserId
    override val hasUser: Boolean = accountService.hasUser
    override val currentUser: Flow<User> = accountService.currentUser

    override suspend fun authenticate(email: String, password: String) =
        accountService.authenticate(email, password)

    override suspend fun sendRecoveryEmail(email: String) = accountService.sendRecoveryEmail(email)

    override suspend fun createAnonymousAccount() = accountService.createAnonymousAccount()

    override suspend fun linkAccount(email: String, password: String) =
        accountService.linkAccount(email, password)

    override suspend fun deleteAccount() = accountService.deleteAccount()

    override suspend fun signOut() = accountService.signOut()
}
