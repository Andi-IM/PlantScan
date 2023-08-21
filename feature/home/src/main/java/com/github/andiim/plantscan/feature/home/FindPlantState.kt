package com.github.andiim.plantscan.feature.home

sealed class FindPlantState<out R> private constructor() {
  data class Success<out T>(val data: T) : FindPlantState<T>()
  data class Error(val message: String) : FindPlantState<Nothing>()
  object Empty : FindPlantState<Nothing>()
}
