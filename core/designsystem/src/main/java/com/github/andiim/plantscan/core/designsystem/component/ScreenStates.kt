package com.github.andiim.plantscan.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.andiim.plantscan.core.designsystem.theme.PlantScanTheme

@Composable
fun LoadingState(message: String? = null, percent: Float? = null) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        if (percent != null) CircularProgressIndicator(progress = percent)
        else CircularProgressIndicator()
        if (message != null)
            Text(message, modifier = Modifier.align(Alignment.BottomCenter).padding(50.dp))
    }
}

@Preview
@Composable
private fun Preview_LoadingContent() {
    PlantScanTheme { Surface { LoadingState() } }
}

@Composable
fun ErrorState(message: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Error: ${message ?: "undefined"}")
        Button(onClick = {}) { Text("Try Again") }
    }
}

@Preview
@Composable
private fun Preview_ErrorState() {
    PlantScanTheme { Surface { ErrorState("Something Error!") } }
}