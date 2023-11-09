package com.github.andiim.plantscan.feature.detect.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetectDetailScreen(
    onBackClick: () -> Unit,
    onSuggestClick: () -> Unit,
    uiState: DetectDetailUiState,
) {
    val scrollState = rememberLazyListState()
    var showPreview by remember { mutableStateOf(false) }
    Box {
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
                        val date = uiState.history.timeStamp.toFormattedDate()
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
