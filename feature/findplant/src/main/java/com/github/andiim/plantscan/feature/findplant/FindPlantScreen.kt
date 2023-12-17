package com.github.andiim.plantscan.feature.findplant

import android.util.Log
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
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
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
import com.github.andiim.plantscan.core.designsystem.component.RoundIconButton
import com.github.andiim.plantscan.core.designsystem.component.scrollbar.DraggableScrollbar
import com.github.andiim.plantscan.core.designsystem.component.scrollbar.rememberDraggableScroller
import com.github.andiim.plantscan.core.designsystem.component.scrollbar.scrollbarState
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.core.designsystem.theme.PsTheme
import com.github.andiim.plantscan.core.model.data.Plant
import com.github.andiim.plantscan.core.ui.DevicePreviews
import com.github.andiim.plantscan.core.ui.PlantCard
import com.github.andiim.plantscan.core.ui.TrackScreenViewEvent
import com.github.andiim.plantscan.feature.findplant.R.string as AppText

@Composable
internal fun FindPlantRoute(
    onPlantClick: (String) -> Unit,
    onCameraClick: () -> Unit,
    onPlantsClick: () -> Unit,
    modifier: Modifier = Modifier,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FindPlantScreen(
    modifier: Modifier = Modifier,
    onClearRecentSearches: () -> Unit = {},
    onSearchTriggered: (String) -> Unit = {},
    onQueryChanged: (String) -> Unit = {},
    searchQuery: String = "",
    onPlantClick: (String) -> Unit = {},
    onCameraClick: () -> Unit = {},
    onPlantsClick: () -> Unit = {},
    recentSearchUiState: RecentSearchQueriesUiState = RecentSearchQueriesUiState.Loading,
    searchResultUiState: SearchResultUiState = SearchResultUiState.Loading,
) {
    var active by rememberSaveable { mutableStateOf(false) }

    TrackScreenViewEvent(screenName = "FindPlant")
    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true },
    ) {
        SearchContent(
            query = searchQuery,
            onQueryChange = onQueryChanged,
            onSearch = onSearchTriggered,
            active = active,
            onActiveChange = { status -> active = status },
            onItemClick = onPlantClick,
            onClearRecentSearches = onClearRecentSearches,
            recentSearchUiState = recentSearchUiState,
            searchResultUiState = searchResultUiState,
            modifier = Modifier.align(Alignment.TopCenter),
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text(stringResource(R.string.camera_button_tooltip))
                    }
                },
                state = rememberTooltipState(),
            ) {
                RoundIconButton(
                    imageVector = PsIcons.Camera,
                    contentDescription = stringResource(R.string.camera_button_tooltip),
                    onClick = onCameraClick,
                )
            }
        }

        val fraction = 0.8f
        PsButton(
            onClick = onPlantsClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(fraction)
                .padding(horizontal = 16.dp),
        ) {
            Text(text = "To list")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("detekt:LongMethod")
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
        placeholder = { Text(stringResource(R.string.find_plant_placeholder)) },
        leadingIcon = {
            Icon(PsIcons.Search, contentDescription = stringResource(R.string.testing))
        },
        trailingIcon = {
            if (active) {
                IconButton(
                    onClick = {
                        onActiveChange(false)
                        onQueryChange("")
                    },
                ) {
                    Icon(
                        PsIcons.Close,
                        contentDescription = stringResource(AppText.search_icon_close_description),
                    )
                }
            }
        },
        modifier = modifier,
    ) {
        Log.d("SearchResult", "SearchContent: $searchResultUiState")
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
                        recentSearchQueries = SearchQueries(recentSearchUiState.recentQueries.map { it.query }),
                    )
                }
            }

            is SearchResultUiState.Success -> {
                if (searchResultUiState.plants.isEmpty()) {
                    EmptySearchResultBody(searchQuery = query)
                    if (recentSearchUiState is RecentSearchQueriesUiState.Success) {
                        RecentSearchesBody(
                            onClearRecentSearches = onClearRecentSearches,
                            onRecentSearchClicked = {
                                onQueryChange(it)
                                onSearch(it)
                            },
                            recentSearchQueries = SearchQueries(recentSearchUiState.recentQueries.map { it.query }),
                        )
                    }
                } else {
                    SearchResultBody(
                        onPlantClick = onItemClick,
                        plants = Plants(searchResultUiState.plants),
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

@Immutable
data class SearchQueries(val items: List<String>)

@Composable
fun RecentSearchesBody(
    modifier: Modifier = Modifier,
    onClearRecentSearches: () -> Unit = {},
    onRecentSearchClicked: (String) -> Unit = {},
    recentSearchQueries: SearchQueries = SearchQueries(listOf()),
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(R.string.recent_searches))
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            if (recentSearchQueries.items.isNotEmpty()) {
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
        }
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            items(recentSearchQueries.items) { recentSearch ->
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

@Composable
fun EmptySearchResultBody(
    searchQuery: String,
    modifier: Modifier = Modifier,
) {
    val message = stringResource(id = AppText.search_result_not_found, searchQuery)
    val start = message.indexOf(searchQuery)
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        textAlign = TextAlign.Center,
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

@Immutable
data class Plants(val items: List<Plant>)

@Composable
private fun SearchResultBody(
    plants: Plants,
    onPlantClick: (String) -> Unit,
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
            if (plants.items.isNotEmpty()) {
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
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
                plants.items.forEach { plant ->
                    val plantId = plant.id
                    item(
                        key = "plant-$plantId",
                        span = {
                            GridItemSpan(maxLineSpan)
                        },
                    ) {
                        PlantCard(plant = plant, onClick = { onPlantClick(plantId) })
                    }
                }
            }
        }
        val itemsAvailable = plants.items.size
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

@DevicePreviews
@Composable
fun Preview_CameraContent() {
    PsTheme {
        Surface {
            FindPlantScreen(
                onSearchTriggered = {},
                onQueryChanged = {},
                onPlantClick = {},
                onCameraClick = { /*TODO*/ },
                onPlantsClick = { /*TODO*/ },
            )
        }
    }
}
