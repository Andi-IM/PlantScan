package com.github.andiim.plantscan.core.auth

import com.github.andiim.plantscan.core.auth.model.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementation of `AuthHelper` which logs events to a Firebase backend.
 */
class FirebaseAuthHelper @Inject constructor(
    private val auth: FirebaseAuth,
) : AuthHelper {
    override val currentUserId: String = auth.currentUser?.uid.orEmpty()
    override val hasUser: Boolean = auth.currentUser != null
    override val isAnonymous: Boolean = auth.currentUser?.isAnonymous ?: true
    override val currentUser: Flow<User> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(
                auth.currentUser?.let { User(it.uid, it.isAnonymous) } ?: User(),
            )
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun sendRecoveryEmail(email: String) {
        auth.sendPasswordResetEmail(email)
    }

    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously().await()
    }

    override suspend fun linkAccount(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser!!.linkWithCredential(credential).await()
    }

    override suspend fun deleteAccount() {
        auth.currentUser!!.delete().await()
    }

    override suspend fun signOut() {
        if (auth.currentUser!!.isAnonymous) {
            auth.currentUser!!.delete()
            createAnonymousAccount()
        } else {
            auth.signOut()
        }
    }
}
