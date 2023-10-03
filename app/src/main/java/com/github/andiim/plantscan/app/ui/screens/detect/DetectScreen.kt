package com.github.andiim.plantscan.app.ui.screens.detect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
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

    LaunchedEffect(viewModel) { viewModel.detect(context) }
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
                DetectSuccessComponent(scrollState, detectState, onSuggestClick)
            }
        }
        BasicToolbar(
            title =
            when (detectState) {
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
        Surface { LazyColumn { detectContent(onSuggestClick = {}, result = detection) } }
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
