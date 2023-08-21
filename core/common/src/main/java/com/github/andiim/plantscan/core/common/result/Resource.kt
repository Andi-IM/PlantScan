package com.github.andiim.plantscan.core.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class Resource<out R> private constructor() {
  data class Success<out T>(val data: T) : Resource<T>()
  data class Error(val exception: Throwable? = null) : Resource<Nothing>()
  data class Loading(val percentage: Float? = null, val status: String? = null) :
      Resource<Nothing>()
}

fun <T> Flow<T>.asResult(): Flow<Resource<T>> {
  return this.map<T, Resource<T>> { Resource.Success(it) }
      .onStart { emit(Resource.Loading()) }
      .catch { emit(Resource.Error(it)) }
}
