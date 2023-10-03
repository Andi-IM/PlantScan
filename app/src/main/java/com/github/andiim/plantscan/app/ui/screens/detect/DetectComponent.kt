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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.core.domain.model.ObjectDetection

@Composable
fun DetectSuccessComponent(
    scrollState: LazyListState,
    detectState: DetectUiState.Success,
    onSuggestClick: () -> Unit
) {
    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
    LazyColumn(state = scrollState, horizontalAlignment = Alignment.CenterHorizontally) {
        detectContent(
            result = detectState.detection,
            onSuggestClick = onSuggestClick,
        )
    }
    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
}

fun LazyListScope.detectContent(
    result: ObjectDetection,
    onSuggestClick: () -> Unit,
) {
    val predictionList =
        result.predictions.map { DetectionResult(it.jsonMemberClass, it.confidence.times(100)) }
    item {
        var enabled by remember { mutableStateOf(true) }
        DetectImage(result = result) { enabled = !enabled }
    }
    item { Text(text = "Results") }

    if (predictionList.isEmpty()) {
        item { Text(text = "Can't detect or not available in dataset!") }
    }

    predictionList.forEach {
        item {
            ListItem(
                headlineContent = { Text(it.name) },
                trailingContent = { Text(text = "${it.conf}%") }
            )
        }
    }

    item { SuggestButton(onClick = onSuggestClick) }
}

@Composable
private fun DetectImage(result: ObjectDetection, onClick: () -> Unit) {
    val imageSize = 32
    SubcomposeAsyncImage(
        model =
        ImageRequest.Builder(LocalContext.current)
            .data(result.image.data)
            .size(imageSize)
            .crossfade(true)
            .build(),
        loading = {
            if (LocalInspectionMode.current) {
                ImageInspection()
            } else {
                Box(modifier = Modifier.height(30.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.width(24.dp))
                }
            }
        },
        error = {
            Box(modifier = Modifier.height(30.dp), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Dangerous,
                    contentDescription = stringResource(R.string.fetch_image_error)
                )
            }
        },
        contentDescription = "image",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier.fillMaxWidth().animateContentSize().clickable { onClick() }
    )
}

@Composable
fun ImageInspection() {
    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.orchid),
            contentScale = ContentScale.FillHeight,
            contentDescription = null
        )
    }
}

@Composable
fun SuggestButton(onClick: () -> Unit) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle()) { append(context.getString(R.string.suggestion_button_label)) }
        append(" ")
        withStyle(
            style =
            SpanStyle(color = (MaterialTheme.colorScheme).primary, fontWeight = FontWeight.Bold),
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
fun LoadingState(
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
            Text(message, modifier = modifier.padding(50.dp))
        }
    }
}

@Composable
fun ErrorState(message: String, onClick: () -> Unit = {}) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Error: $message")
            Button(onClick = onClick) { Text("Try Again") }
        }
    }
}
