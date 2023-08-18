package com.github.andiim.plantscan.app.core.data

sealed class Resource<out R> private constructor() {
  data class Success<out T>(val data: T) : Resource<T>()
  data class Error(val message: String) : Resource<Nothing>()
  data class Loading(val status: Float) : Resource<Nothing>()
  object Empty : Resource<Nothing>()
}
