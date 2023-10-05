package com.github.andiim.plantscan.app.core.result

import com.google.android.gms.tasks.Task
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    data class Loading(val progress: String? = null) : Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading()) }
        .catch { emit(Result.Error(it)) }
}

suspend fun UploadTask.asResult(): Task<Result<String>> {
    return suspendCancellableCoroutine {
        val progressListener = OnProgressListener<UploadTask.TaskSnapshot> { taskSnapshot ->
            val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
            Timber.d("UPDATED $progress")
            Result.Loading(progress.toString())
            return@OnProgressListener
        }

        // Add the progress listener to the UploadTask
        addOnProgressListener(progressListener)

        // Await the completion of the UploadTask
        addOnCompleteListener { task ->
            removeOnProgressListener(progressListener)
            if (task.isSuccessful) {
                val downloadUrl = task.result?.storage?.downloadUrl?.toString()
                if (downloadUrl != null) {
                    Result.Success(downloadUrl)
                } else {
                    Result.Error(Exception("Something went wrong"))
                }
            } else {
                Result.Error(task.exception)
            }
            return@addOnCompleteListener
        }
    }
}
