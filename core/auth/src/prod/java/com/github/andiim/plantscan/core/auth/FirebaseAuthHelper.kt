package com.github.andiim.plantscan.core.auth

import android.util.Log
import com.github.andiim.plantscan.core.auth.model.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementation of `AuthHelper` which logs events to a Firebase backend.
 */
class FirebaseAuthHelper @Inject constructor(
    private val auth: FirebaseAuth,
) : AuthHelper {
    override val currentUser: Flow<User> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(
                auth.currentUser?.let { User(it.uid, it.isAnonymous) } ?: User(),
            )
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun authenticate(email: String, password: String): Flow<Unit> = flow {
        auth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun sendRecoveryEmail(email: String): Flow<Unit> = flow {
        auth.sendPasswordResetEmail(email)
    }

    override fun createAnonymousAccount(): Flow<Unit> = flow {
        auth.signInAnonymously().await()
    }

    override fun linkAccount(email: String, password: String): Flow<Unit> = flow {
        val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser!!.linkWithCredential(credential).await()
    }

    override fun deleteAccount(): Flow<Unit> = flow {
        auth.currentUser!!.delete().await()
    }

    override fun signOut(): Flow<Unit> = flow {
        if (auth.currentUser!!.isAnonymous) {
            auth.currentUser!!.delete()
            createAnonymousAccount()
        } else {
            auth.signOut()
        }
    }
}
