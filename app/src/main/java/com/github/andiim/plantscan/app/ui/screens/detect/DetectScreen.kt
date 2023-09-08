package com.github.andiim.plantscan.app.ui.screens.detect

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.core.domain.model.ObjectDetection
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import com.github.andiim.plantscan.app.utils.DetectScreenPreviewParameterProvider

@Composable
fun DetectRoute(
    backToTop: () -> Unit,
    onSuggestClick: (String) -> Unit,
    viewModel: DetectViewModel = hiltViewModel()
) {
    val detectState: DetectUiState by viewModel.detectResult.collectAsState()
    val context = LocalContext.current

    DetectScreen(
        context = context,
        detectState = detectState,
        detect = viewModel::detect,
        onBackClick = backToTop,
        onSuggestClick = { viewModel.onSuggestClick(onSuggestClick) },
    )


    LaunchedEffect(viewModel) {
        viewModel.detect(context)
    }
}

@Composable
fun DetectScreen(
    context: Context,
    onBackClick: () -> Unit,
    onSuggestClick: () -> Unit,
    detectState: DetectUiState,
    detect: (Context) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (detectState) {
            is DetectUiState.Error -> {
                ErrorState(detectState.message ?: "", onClick = { detect(context) })
            }

            DetectUiState.Loading -> {
                LoadingState("Detecting... ")
            }

            is DetectUiState.Success -> {
                DetectContent(
                    result = detectState.detection,
                    onBackClick = onBackClick,
                    onSuggestClick = onSuggestClick,
                )
            }
        }
    }
}

@Composable
fun DetectContent(
    result: ObjectDetection,
    onBackClick: () -> Unit,
    onSuggestClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val predictionList = result.predictions.map {
            DetectionResult(it.jsonMemberClass, it.confidence.times(100))
        }

        item {
            Button(onClick = onBackClick) {
                Text(text = "Back")
            }
        }

        item {
            var enabled by remember { mutableStateOf(true) }

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(result.image.data)
                    .size(32)
                    .crossfade(true)
                    .build(),
                loading = {
                    if (LocalInspectionMode.current) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        )
                        {
                            Image(
                                painter = painterResource(R.drawable.orchid),
                                contentScale = ContentScale.FillHeight,
                                contentDescription = null
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier.height(30.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.width(24.dp))
                        }
                    }
                },
                error = {
                    Box(
                        modifier = Modifier.height(30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dangerous,
                            contentDescription = stringResource(R.string.fetch_image_error)
                        )
                    }
                },
                contentDescription = "image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .clickable { enabled = !enabled }
            )
        }

        item {
            Text(text = "Results")
        }
        predictionList.forEach {
            item {
                ListItem(
                    headlineContent = { Text(it.name) },
                    trailingContent = { Text(text = "${it.conf}%") })
            }
        }

        item {
            SuggestButton(onClick = onSuggestClick)
        }
    }
}

@Composable
fun SuggestButton(onClick: () -> Unit) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle()) {
            append(context.getString(R.string.suggestion_button_label))
        }
        append(" ")
        withStyle(
            style = SpanStyle(
                color = (MaterialTheme.colorScheme).primary,
                fontWeight = FontWeight.Bold
            ),
        ) {
            append(context.getString(R.string.suggestion_action_label))
        }
    }

    ClickableText(
        text = annotatedText,
        onClick = { onClick() },
    )

}


data class DetectionResult(val name: String, val conf: Float)

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
private fun ErrorState(message: String, onClick: () -> Unit = {}) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Error: $message")
            Button(onClick = onClick) { Text("Try Again") }
        }

    }
}

@Preview
@Composable
private fun Preview_LoadingContent() {
    PlantScanTheme { Surface { LoadingState() } }
}

@Preview
@Composable
private fun Preview_DetectContent(
    @PreviewParameter(DetectScreenPreviewParameterProvider::class) detection: ObjectDetection
) {
    // PlantScanTheme { Surface { DetectContent(detection) } }
}

@Preview
@Composable
private fun Preview_ErrorState() {
    PlantScanTheme { Surface { ErrorState("Something Error!") } }
}
