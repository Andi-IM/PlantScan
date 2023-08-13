package com.github.andiim.plantscan.app.core.data.source.firebase.firestore

import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class CurrentUser(val uid: String = "", val isAnonymous: Boolean = true)

interface AccountServiceManipulator {
  var currentUser: CurrentUser?
  var hasError: Boolean
  suspend fun signIn(): Flow<Any>
  suspend fun anonymousSignIn()
  fun signOut()
  fun get()

  fun showError()
}

class AccountServiceManipulatorImpl @Inject constructor() : AccountServiceManipulator {
  override var hasError: Boolean = false
  override var currentUser: CurrentUser? = null

  override fun showError() {
    hasError = true
  }
  override suspend fun signIn(): Flow<Any> = flow {
    if (hasError) throw Exception("Error!")

    val uid = UUID.randomUUID().toString()
    currentUser = CurrentUser(uid = uid, isAnonymous = false)
  }

  override suspend fun anonymousSignIn() {
    print("HAS ERROR $hasError")
    if (hasError) throw Exception("Error!")

    val uid = UUID.randomUUID().toString()
    currentUser = CurrentUser(uid = uid, isAnonymous = true)
  }

  override fun signOut() {
    currentUser = null
  }

  override fun get() {
    TODO("Not yet implemented")
  }
}
