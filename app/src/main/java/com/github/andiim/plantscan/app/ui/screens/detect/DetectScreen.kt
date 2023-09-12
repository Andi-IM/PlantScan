package com.github.andiim.plantscan.app.ui.screens.detect

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.github.andiim.plantscan.app.ui.common.DetectScreenPreviewParameterProvider
import com.github.andiim.plantscan.app.ui.common.composables.BasicToolbar
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme

@Composable
fun DetectRoute(
    backToTop: () -> Unit,
    onSuggestClick: (String) -> Unit,
    viewModel: DetectViewModel = hiltViewModel()
) {
    val detectState: DetectUiState by viewModel.detectResult.collectAsState()
    val context = LocalContext.current

    DetectScreen(
        detectState = detectState,
        detect = { viewModel.detect(context) },
        onBackClick = backToTop,
        onSuggestClick = { viewModel.onSuggestClick(onSuggestClick) },
    )

    LaunchedEffect(viewModel) {
        viewModel.detect(context)
    }
}

@Composable
fun DetectScreen(
    onBackClick: () -> Unit,
    onSuggestClick: () -> Unit,
    detectState: DetectUiState,
    detect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    Box(modifier = modifier.fillMaxSize()) {
        when (detectState) {
            is DetectUiState.Error -> {
                ErrorState(detectState.message ?: "", onClick = { detect() })
            }

            DetectUiState.Loading -> {
                LoadingState(
                    stringResource(R.string.detect_loading_state),
                    modifier = modifier.align(Alignment.Center)
                )
            }

            is DetectUiState.Success -> {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                LazyColumn(
                    state = scrollState,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    detectContent(
                        result = detectState.detection,
                        onSuggestClick = onSuggestClick,
                    )
                }
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }
        }
        BasicToolbar(
            title = when (detectState) {
                is DetectUiState.Error -> R.string.detect_error_title
                DetectUiState.Loading -> R.string.detect_loading_title
                is DetectUiState.Success -> R.string.detect_screen_title
            },
            leading = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
                }
            },
        )
    }
}

fun LazyListScope.detectContent(
    result: ObjectDetection,
    onSuggestClick: () -> Unit,
) {
    val predictionList = result.predictions.map {
        DetectionResult(it.jsonMemberClass, it.confidence.times(100))
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
                    ) {
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
                trailingContent = { Text(text = "${it.conf}%") }
            )
        }
    }

    item {
        SuggestButton(onClick = onSuggestClick)
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
private fun LoadingState(
    message: String,
    modifier: Modifier = Modifier,
    percent: Float? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        if (percent != null) {
            CircularProgressIndicator(progress = percent)
        } else {
            CircularProgressIndicator()
        }
        if (message.isNotEmpty()) {
            Text(
                message,
                modifier = modifier
                    .padding(50.dp)
            )
        }
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
    PlantScanTheme {
        Surface {
            DetectScreen(
                onBackClick = {},
                onSuggestClick = {},
                detectState = DetectUiState.Loading,
                detect = {}
            )
        }
    }
}

@Preview
@Composable
private fun Preview_SuccessContent(
    @PreviewParameter(DetectScreenPreviewParameterProvider::class) detection: ObjectDetection
) {
    PlantScanTheme {
        Surface {
            DetectScreen(
                onBackClick = {},
                onSuggestClick = {},
                detectState = DetectUiState.Success(detection),
                detect = {}
            )
        }
    }
}

@Preview
@Composable
private fun Preview_DetectContent(
    @PreviewParameter(DetectScreenPreviewParameterProvider::class) detection: ObjectDetection
) {
    PlantScanTheme {
        Surface {
            LazyColumn {
                detectContent(
                    onSuggestClick = {},
                    result = detection
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview_ErrorState() {
    PlantScanTheme {
        Surface {
            DetectScreen(
                onBackClick = {},
                onSuggestClick = {},
                detectState = DetectUiState.Error("Something Error!"),
                detect = {}
            )
        }
    }
}
