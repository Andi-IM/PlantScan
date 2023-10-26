package com.github.andiim.plantscan.feature.web

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class WebViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val webArgs : WebArgs = WebArgs(savedStateHandle)
    val webUrl: String = webArgs.url
}