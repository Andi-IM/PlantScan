package com.github.andiim.plantscan.app.core.firestore

sealed class DbResponse<out R> {
    data class Success<out T>(val data: T) : DbResponse<T>()
    data class Error(val errorMessage: String) : DbResponse<Nothing>()
    data object Empty : DbResponse<Nothing>()
}
