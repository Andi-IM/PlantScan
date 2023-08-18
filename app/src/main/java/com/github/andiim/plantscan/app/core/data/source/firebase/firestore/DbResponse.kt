package com.github.andiim.plantscan.app.core.data.source.firebase.firestore

sealed class DbResponse<out R> {
  data class Success<out T>(val data: T) : DbResponse<T>()
  data class Error(val errorMessage: String) : DbResponse<Nothing>()
  object Empty : DbResponse<Nothing>()
}
