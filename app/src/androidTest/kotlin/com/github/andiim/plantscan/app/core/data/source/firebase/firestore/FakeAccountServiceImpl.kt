package com.github.andiim.plantscan.app.core.data.source.firebase.firestore

import com.github.andiim.plantscan.app.core.domain.model.User
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.AccountService
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAccountServiceImpl @Inject constructor(private val auth: AccountServiceManipulator) :
    AccountService {
  override val currentUserId: String
    get() = auth.currentUser?.uid.orEmpty()
  override val hasUser: Boolean
    get() = auth.currentUser != null
  override val currentUser: Flow<User>
    get() = flow { auth.currentUser?.let { emit(User(it.uid, it.isAnonymous)) } }

  override suspend fun authenticate(email: String, password: String) {
    auth.signIn()
  }

  override suspend fun sendRecoveryEmail(email: String) {
    TODO("Not yet implemented")
  }

  override suspend fun createAnonymousAccount() {
    auth.anonymousSignIn()
  }

  override suspend fun linkAccount(email: String, password: String) {
    TODO("Not yet implemented")
  }

  override suspend fun deleteAccount() = auth.signOut()

  override suspend fun signOut() = auth.signOut()
}
