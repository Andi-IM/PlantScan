package com.github.andiim.plantscan.feature.findplant

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.andiim.plantscan.core.designsystem.component.PsButton
import com.github.andiim.plantscan.core.designsystem.component.scrollbar.DraggableScrollbar
import com.github.andiim.plantscan.core.designsystem.component.scrollbar.rememberDraggableScroller
import com.github.andiim.plantscan.core.designsystem.component.scrollbar.scrollbarState
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.core.model.data.Plant
import com.github.andiim.plantscan.core.ui.TrackScreenViewEvent
import com.github.andiim.plantscan.feature.findplant.R.string as AppText

@Composable
internal fun FindPlantRoute(
    modifier: Modifier = Modifier,
    onPlantClick: (String) -> Unit,
    onCameraClick: () -> Unit,
    onPlantsClick: () -> Unit,
    viewModel: FindPlantViewModel = hiltViewModel(),
) {
    val recentSearchQueriesUiState by viewModel.recentSearchQueriesUiState.collectAsStateWithLifecycle()
    val searchResultUiState by viewModel.searchResultUiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    FindPlantScreen(
        modifier = modifier,
        onClearRecentSearches = viewModel::clearRecentSearches,
        onSearchTriggered = viewModel::onSearchTriggered,
        onQueryChanged = viewModel::onSearchQueryChanged,
        searchQuery = searchQuery,
        onPlantClick = onPlantClick,
        onPlantsClick = onPlantsClick,
        onCameraClick = onCameraClick,
        recentSearchUiState = recentSearchQueriesUiState,
        searchResultUiState = searchResultUiState,
    )
}

@Composable
internal fun FindPlantScreen(
    modifier: Modifier = Modifier,
    onClearRecentSearches: () -> Unit = {},
    onSearchTriggered: (String) -> Unit,
    onQueryChanged: (String) -> Unit,
    searchQuery: String = "",
    onPlantClick: (String) -> Unit,
    onCameraClick: () -> Unit,
    onPlantsClick: () -> Unit,
    recentSearchUiState: RecentSearchQueriesUiState = RecentSearchQueriesUiState.Loading,
    searchResultUiState: SearchResultUiState = SearchResultUiState.Loading,
) {
    var active by rememberSaveable { mutableStateOf(false) }

    TrackScreenViewEvent(screenName = "FindPlant")
    Box(
        modifier = modifier,
    ) {
        PsButton(onClick = onCameraClick) {
            Text(text = "To Camera")
        }

        PsButton(onClick = onPlantsClick) {
            Text(text = "To list")
        }

        SearchContent(
            query = searchQuery,
            onQueryChange = onQueryChanged,
            onSearch = onSearchTriggered,
            active = active,
            onActiveChange = { active = !active },
            onItemClick = onPlantClick,
            onClearRecentSearches = onClearRecentSearches,
            recentSearchUiState = recentSearchUiState,
            searchResultUiState = searchResultUiState,
            modifier = modifier.align(Alignment.TopCenter)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onClearRecentSearches: () -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    recentSearchUiState: RecentSearchQueriesUiState = RecentSearchQueriesUiState.Loading,
    searchResultUiState: SearchResultUiState = SearchResultUiState.Loading,
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        leadingIcon = {
            Icon(PsIcons.Search, contentDescription = stringResource(R.string.testing))
        },
        trailingIcon = {
            if (active) {
                IconButton(onClick = { onActiveChange(false) }) {
                    Icon(
                        PsIcons.Close,
                        contentDescription = stringResource(AppText.search_icon_close_description),
                    )
                }
            }
        },
        modifier = modifier,
    ) {
        when (searchResultUiState) {
            SearchResultUiState.Loading,
            SearchResultUiState.LoadFailed,
            -> Unit
            SearchResultUiState.SearchNotReady -> SearchNotReadyBody()
            SearchResultUiState.EmptyQuery -> {
                if (recentSearchUiState is RecentSearchQueriesUiState.Success) {
                    RecentSearchesBody(
                        onClearRecentSearches = onClearRecentSearches,
                        onRecentSearchClicked = {
                            onQueryChange(it)
                            onSearch(it)
                        },
                        recentSearchQueries = recentSearchUiState.recentQueries.map { it.query },
                    )
                }
            }
            is SearchResultUiState.Success -> {
                if (searchResultUiState.isEmpty()) {
                    EmptySearchResultBody(searchQuery = query)
                    if (recentSearchUiState is RecentSearchQueriesUiState.Success) {
                        RecentSearchesBody(
                            onClearRecentSearches = onClearRecentSearches,
                            onRecentSearchClicked = {
                                onQueryChange(it)
                                onSearch(it)
                            },
                            recentSearchQueries = recentSearchUiState.recentQueries.map { it.query },
                        )
                    }
                } else {
                    SearchResultBody(
                        onPlantClick = onItemClick,
                        plants = searchResultUiState.plants,
                        searchQuery = query,
                    )
                }
            }
        }
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
    }
}

@Composable
private fun SearchNotReadyBody() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 48.dp),
    ) {
        Text(
            text = "",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 14.dp),
        )
    }
}

@Composable
fun RecentSearchesBody(
    onClearRecentSearches: () -> Unit,
    onRecentSearchClicked: (String) -> Unit,
    recentSearchQueries: List<String>,
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("")
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            if (recentSearchQueries.isNotEmpty()) {
                IconButton(
                    onClick = onClearRecentSearches,
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    Icon(
                        imageVector = PsIcons.Close,
                        contentDescription = stringResource(AppText.search_clear_search_text),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(recentSearchQueries) { recentSearch ->
                    Text(
                        text = recentSearch,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .clickable {
                                onRecentSearchClicked(recentSearch)
                            }
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
fun EmptySearchResultBody(
    searchQuery: String,
) {
    val message = stringResource(id = AppText.search_icon_close_description, searchQuery)
    val start = message.indexOf(searchQuery)
    Text(
        text = AnnotatedString(
            text = message,
            spanStyles = listOf(
                AnnotatedString.Range(
                    SpanStyle(fontWeight = FontWeight.Bold),
                    start = start,
                    end = start + searchQuery.length,
                ),
            ),
        ),
    )
}

@Composable
private fun SearchResultBody(
    plants: List<Plant>,
    onPlantClick: (String) -> Unit,
    searchQuery: String = "",
) {
    val state = rememberLazyGridState()
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("search:plant"),
            state = state,
        ) {
            if (plants.isNotEmpty()) {
                item(
                    span = {
                        GridItemSpan(maxLineSpan)
                    },
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Plants")
                            }
                        }
                    )
                }
                plants.forEach { plant ->
                    val plantId = plant.name
                }
            }
        }
        val itemsAvailable = plants.size
        val scrollbarState = state.scrollbarState(
            itemsAvailable = itemsAvailable,
        )
        state.DraggableScrollbar(
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 2.dp)
                .align(Alignment.CenterEnd),
            state = scrollbarState,
            orientation = Orientation.Vertical,
            onThumbMoved = state.rememberDraggableScroller(itemsAvailable = itemsAvailable),
        )
    }
}
