package com.github.andiim.plantscan.feature.suggest

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SuggestRoute(
    onBackPressed: () -> Unit,
    onShowSnackbar: suspend (String, String?, SnackbarDuration?) -> Boolean,
    viewModel: SuggestViewModel = hiltViewModel(),
) {

}

@Composable
fun SuggestScreen() {

}