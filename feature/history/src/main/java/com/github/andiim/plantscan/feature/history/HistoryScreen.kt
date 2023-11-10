package com.github.andiim.plantscan.feature.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.plantscan.core.model.data.DetectionHistory
import com.github.andiim.plantscan.core.ui.TrackScreenViewEvent
import com.github.andiim.plantscan.core.ui.TrackScrollJank
import com.github.andiim.plantscan.core.ui.extensions.toFormattedDate
import com.github.andiim.plantscan.feature.history.navigation.History

@Composable
internal fun HistoryRoute(
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val historyUiState by viewModel.historyUiState.collectAsState()
    TrackScreenViewEvent(screenName = History.route)
    HistoryScreen(
        historyState = historyUiState,
        onItemClick = onItemClick,
        modifier = modifier,
    )
}

@Composable
internal fun HistoryScreen(
    historyState: HistoryUiState,
    onItemClick: (String) -> Unit,
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
                is HistoryUiState.Error -> handleError(
                    "${historyState.throwable}",
                    modifier.align(Alignment.Center),
                )

                is HistoryUiState.Success -> handleSuccess(
                    historyState.data,
                    onItemClick = onItemClick,
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

@OptIn(ExperimentalMaterial3Api::class)
private fun LazyListScope.handleSuccess(
    detections: List<DetectionHistory>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (detections.isNotEmpty()) {
        items(
            items = detections,
            key = { "${it.id}${it.plantRef}" },
            itemContent = { history ->
                val acc = history.accuracy * 100
                val time = history.timestamp.toFormattedDate()
                Card(
                    onClick = { onItemClick(history.id!!) },
                    shape = RectangleShape,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                    elevation = CardDefaults.cardElevation(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                ) {
                    ListItem(
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent,
                        ),
                        headlineContent = { Text(history.plantRef) },
                        supportingContent = { Text(time) },
                        trailingContent = { Text("%.2f%%".format(acc)) },
                    )
                }
            },
        )
    } else {
        item { Text(text = "Empty List!", modifier = modifier.fillParentMaxSize()) }
    }
}
