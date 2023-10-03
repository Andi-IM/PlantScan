package com.github.andiim.plantscan.app.ui.screens.home.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.core.ui.TrackScrollJank
import com.github.andiim.plantscan.app.ui.TrackScreenViewEvent
import com.github.andiim.plantscan.app.ui.common.HistoryScreenPreviewParameterProvider
import com.github.andiim.plantscan.app.ui.common.composables.BasicToolbar
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber

@Composable
fun HistoryRoute(
    toDetail: (String) -> Unit,
    viewModel: MyGardenViewModel = hiltViewModel()
) {
    val historyUiState: HistoryUiState by viewModel.historyUiState.collectAsState()

    TrackScreenViewEvent(screenName = "History")
    HistoryScreen(
        historyUiState = historyUiState,
        onItemClick = toDetail,
        getId = viewModel::getDetailId
    )

    LaunchedEffect(viewModel) {
        viewModel.fetchHistory()
    }
}

@Composable
internal fun HistoryScreen(
    modifier: Modifier = Modifier,
    historyUiState: HistoryUiState,
    getId: (String) -> String? = { _ -> null },
    onItemClick: (String) -> Unit = {},
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
                BasicToolbar(title = R.string.label_history)
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

                                LaunchedEffect(getId) {
                                    Timber.d("${getId.invoke(history.plantRef)}")
                                }

                                ListItem(
                                    modifier = Modifier.clickable { onItemClick.invoke(history.plantRef) },
                                    headlineContent = { Text(history.plantRef) },
                                    supportingContent = {
                                        Text(
                                            "${
                                                history.timestamp?.toLocalDateTime(
                                                    TimeZone.currentSystemDefault()
                                                )
                                            }"
                                        )
                                    },
                                    trailingContent = { Text("%.2f%%".format(history.accuracy * 100)) }
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
