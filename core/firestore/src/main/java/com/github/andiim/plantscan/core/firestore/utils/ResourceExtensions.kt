package com.github.andiim.plantscan.core.firestore.utils

import com.github.andiim.plantscan.core.common.result.Resource
import com.github.andiim.plantscan.core.common.result.Resource.Error
import com.github.andiim.plantscan.core.common.result.Resource.Loading
import com.github.andiim.plantscan.core.common.result.Resource.Success
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

fun <T> Task<T>.asResult(): Flow<Resource<T>> = flow {
  emit(Loading())
  try {
    val result = await()
    emit(Success(result))
  } catch (e: Exception) {
    emit(Error(e))
  }
}
