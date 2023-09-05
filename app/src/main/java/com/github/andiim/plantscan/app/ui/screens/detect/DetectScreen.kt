package com.github.andiim.plantscan.app.ui.screens.detect

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.DetectResult
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.DecimalFormat

@Composable
fun DetectScreen(imageUri: Uri, viewModel: DetectViewModel = hiltViewModel()) {
    val context = LocalContext.current

    val image =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                decoder.isMutableRequired = true
            }
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }

    when (val state = viewModel.interpreter.collectAsState().value) {
        is Resource.Loading -> {
            LoadingState("Getting Model ")
        }

        is Resource.Error -> {
            ErrorState(state.message)
        }

        is Resource.Success -> {
            val labels = BufferedReader(InputStreamReader(context.assets.open("labels.txt")))
            viewModel.detect(labels, image, state.data)

            when (val detectionState = viewModel.detectResult.collectAsState().value) {
                Resource.Loading -> LoadingState("Trying to Detect...")
                is Resource.Success -> DetectContent(detectionState.data)
                else -> {}
            }
        }
    }

    LaunchedEffect(viewModel) { viewModel.loadInterpreter(context) }
}

@Composable
fun DetectContent(detectResult: DetectResult) {
    val dec = DecimalFormat("#.##")
    val percentage = dec.format(detectResult.prob * 100)
    Column { Text("${detectResult.label} : ${percentage}%") }
}

@Composable
private fun LoadingState(message: String? = null, percent: Float? = null) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        if (percent != null) CircularProgressIndicator(progress = percent)
        else CircularProgressIndicator()
        if (message != null)
            Text(
                message, modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(50.dp)
            )
    }
}

@Composable
private fun ErrorState(message: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Error: $message")
        Button(onClick = {}) { Text("Try Again") }
    }
}

@Preview
@Composable
private fun Preview_LoadingContent() {
    PlantScanTheme { Surface { LoadingState() } }
}

@Preview
@Composable
private fun Preview_DetectContent() {
    PlantScanTheme { Surface { DetectContent(DetectResult("Testing", 0.5f)) } }
}

@Preview
@Composable
private fun Preview_ErrorState() {
    PlantScanTheme { Surface { ErrorState("Something Error!") } }
}
