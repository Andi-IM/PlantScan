package com.github.andiim.plantscan.feature.detect.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.andiim.plantscan.core.designsystem.component.PsTopAppBar
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.core.ui.components.DetectImage
import com.github.andiim.plantscan.core.ui.components.DetectResultImage
import com.github.andiim.plantscan.core.ui.components.SuggestButton
import com.github.andiim.plantscan.core.ui.extensions.toFormattedDate

@Composable
fun DetectDetailRoute(
    onBackClick: () -> Unit,
    onSuggestClick: () -> Unit,
    viewModel: DetectDetailViewModel = hiltViewModel(),
) {
    val result by viewModel.detail.collectAsStateWithLifecycle()
    DetectDetailScreen(
        onBackClick = onBackClick,
        onSuggestClick = onSuggestClick,
        uiState = result,
    )
}

private const val PERCENTAGE = 100

@Suppress("detekt:LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetectDetailScreen(
    uiState: DetectDetailUiState,
    onBackClick: () -> Unit,
    onSuggestClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    var showPreview by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        LazyColumn(state = scrollState, horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }
            when (uiState) {
                DetectDetailUiState.Loading -> item {
                    CircularProgressIndicator()
                }

                is DetectDetailUiState.Success -> {
                    item {
                        val date = uiState.history.timestamp.toFormattedDate()
                        PsTopAppBar(
                            titleRes = R.string.detect_screen_title,
                            navigationIcon = {
                                IconButton(onClick = onBackClick) {
                                    Icon(PsIcons.Back, null)
                                }
                            },
                        )
                        DetectImage(
                            imageData = uiState.history.image,
                            onImageClick = { showPreview = !showPreview },
                        )
                        ListItem(
                            headlineContent = { Text(text = "Timestamp") },
                            trailingContent = {
                                Text(text = date)
                            },
                        )
                        ListItem(
                            headlineContent = { Text(text = "Time for detect") },
                            trailingContent = {
                                Text(text = "${uiState.history.time} ms")
                            },
                        )
                    }
                    item {
                        Text(
                            "Predictions: ",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }
                    uiState.history.detections.forEach { data ->
                        item {
                            val acc = data.confidence.times(PERCENTAGE)
                            ListItem(
                                headlineContent = { Text(text = data.objectClass) },
                                trailingContent = {
                                    Text(text = "%.2f%%".format(acc))
                                },
                            )
                        }
                    }

                    item { SuggestButton(onClick = onSuggestClick) }
                }
            }
        }

        if (showPreview && uiState is DetectDetailUiState.Success) {
            DetectResultImage(
                imageData = uiState.history.image,
                onShowPreview = {
                    showPreview = !showPreview
                },
            )
        }
    }
}
