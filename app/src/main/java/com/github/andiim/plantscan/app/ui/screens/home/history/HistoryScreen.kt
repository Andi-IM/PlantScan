package com.github.andiim.plantscan.app.ui.screens.home.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.plantscan.app.core.ui.TrackScrollJank
import com.github.andiim.plantscan.app.ui.TrackScreenViewEvent
import com.github.andiim.plantscan.app.ui.common.HistoryScreenPreviewParameterProvider
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import timber.log.Timber

@Composable
fun HistoryRoute(
    // toDetail: (String) -> Unit,
    viewModel: MyGardenViewModel = hiltViewModel()
) {
    val historyUiState: HistoryUiState by viewModel.historyUiState.collectAsState()

    Timber.d("STATE $historyUiState")
    TrackScreenViewEvent(screenName = "History")
    HistoryScreen(
        historyUiState = historyUiState,
        // onItemClick = toDetail,
    )

    LaunchedEffect(viewModel) {
        viewModel.fetchHistory()
    }
}

@Composable
internal fun HistoryScreen(
    modifier: Modifier = Modifier,
    historyUiState: HistoryUiState,
    // onItemClick: (String) -> Unit = {}, /* TODO: SOMETIMES CAN SHOW DETAIL */
) {
    val state = rememberLazyListState()
    TrackScrollJank(scrollableState = state, stateName = "detections")
    Box(
        modifier = modifier
    ) {
        LazyColumn(
            state = state,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }
            when (historyUiState) {
                HistoryUiState.Loading -> item {
                    Box(modifier = Modifier.fillParentMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(8.dp)
                                .testTag("loadingWheel")
                        )
                    }
                }

                is HistoryUiState.Error -> {
                    item {
                        Text(
                            text = "${historyUiState.message}",
                            modifier = Modifier
                                .fillParentMaxSize()
                                .align(Alignment.Center)
                        )
                    }
                }

                is HistoryUiState.Success -> {
                    if (historyUiState.detections.isNotEmpty()) {
                        items(
                            items = historyUiState.detections,
                            key = { "${it.id}${it.plantRef}" },
                            itemContent = { history ->
                                ListItem(
                                    headlineContent = { Text(history.plantRef) },
                                    supportingContent = { Text("${history.timestamp}") },
                                    trailingContent = { Text("${history.accuracy}%") }
                                )
                            }
                        )
                    } else {
                        item {
                            Text(
                                text = "Empty List!",
                                modifier = Modifier
                                    .fillParentMaxSize()
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview_MyGardenContent(
    @PreviewParameter(HistoryScreenPreviewParameterProvider::class) uiState: HistoryUiState
) {
    PlantScanTheme {
        Surface {
            HistoryScreen(
                historyUiState = uiState,
            )
        }
    }
}
