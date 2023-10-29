package com.github.andiim.plantscan.feature.history

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.plantscan.core.model.data.DetectionHistory
import com.github.andiim.plantscan.core.ui.TrackScreenViewEvent
import com.github.andiim.plantscan.core.ui.TrackScrollJank
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
internal fun HistoryRoute(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val historyUiState by viewModel.historyUiState.collectAsState()
    TrackScreenViewEvent(screenName = "history")
    HistoryScreen(
        historyState = historyUiState,
        modifier = modifier,
    )
}

@Composable
internal fun HistoryScreen(
    historyState: HistoryUiState,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    TrackScrollJank(scrollableState = scrollState, stateName = "detectionHistory")
    Box(modifier = modifier) {
        LazyColumn(
            state = scrollState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            when (historyState) {
                HistoryUiState.Loading -> handleLoading()
                is HistoryUiState.Error -> handleError("", modifier.align(Alignment.Center))
                is HistoryUiState.Success -> handleSuccess(
                    historyState.data,
                    getId = { _ -> null },
                    onItemClick = {},
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
}

private fun LazyListScope.handleLoading() {
    item {
        Box(modifier = Modifier.fillParentMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(8.dp)
                    .testTag("loadingWheel"),
            )
        }
    }
}

private fun LazyListScope.handleError(message: String, modifier: Modifier) {
    item { Text(text = message, modifier = modifier.fillParentMaxSize()) }
}

private fun LazyListScope.handleSuccess(
    detections: List<DetectionHistory>,
    getId: (String) -> String?,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (detections.isNotEmpty()) {
        items(
            items = detections,
            key = { "${it.id}${it.plantRef}" },
            itemContent = { history ->
                val acc = history.acc * 100

                LaunchedEffect(getId) {
                    Log.d("Get History", "handleSuccess: ${getId.invoke(history.plantRef)}")
                }

                ListItem(
                    modifier = Modifier.clickable { onItemClick.invoke(history.plantRef) },
                    headlineContent = { Text(history.plantRef) },
                    supportingContent = {
                        Text(
                            "${
                                history.timeStamp.toLocalDateTime(
                                    TimeZone.currentSystemDefault(),
                                )
                            }",
                        )
                    },
                    trailingContent = { Text("%.2f%%".format(acc)) },
                )
            },
        )
    } else {
        item { Text(text = "Empty List!", modifier = modifier.fillParentMaxSize()) }
    }
}
