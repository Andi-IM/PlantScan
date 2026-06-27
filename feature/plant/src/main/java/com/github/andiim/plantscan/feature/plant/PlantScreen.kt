package com.github.andiim.plantscan.feature.plant

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.andiim.plantscan.core.designsystem.component.PsBackground
import com.github.andiim.plantscan.core.designsystem.component.scrollbar.DraggableScrollbar
import com.github.andiim.plantscan.core.designsystem.component.scrollbar.rememberDraggableScroller
import com.github.andiim.plantscan.core.designsystem.component.scrollbar.scrollbarState
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.core.designsystem.theme.PsTheme
import com.github.andiim.plantscan.core.ui.TrackScreenViewEvent
import com.github.andiim.plantscan.core.ui.TrackScrollJank
import com.github.andiim.plantscan.core.ui.plantCardItems
import org.jetbrains.annotations.VisibleForTesting

@Composable
internal fun PlantRoute(
    onBackClick: () -> Unit,
    onPlantClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlantViewModel = hiltViewModel(),
) {
    val plantUiState: PlantUiState by viewModel.items.collectAsStateWithLifecycle()
    Log.d("TAG", "PlantRoute: $plantUiState")
    TrackScreenViewEvent(screenName = "Plants")
    PlantScreen(
        plantUiState = plantUiState,
        onBackClick = onBackClick,
        onPlantClick = onPlantClick,
        modifier = modifier,
    )
}

@VisibleForTesting
@Composable
internal fun PlantScreen(
    plantUiState: PlantUiState,
    onPlantClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()
    TrackScrollJank(scrollableState = state, stateName = "plant:screen")
    Box(modifier = modifier) {
        LazyColumn(
            state = state,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            item {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }
            when (plantUiState) {
                PlantUiState.Loading -> item {
                    CircularProgressIndicator(modifier = modifier)
                }

                is PlantUiState.Error -> {}
                is PlantUiState.Success -> {
                    item {
                        PlantToolbar(
                            onBackClick = onBackClick,
                        )
                    }
                    plantBody(
                        plants = plantUiState,
                        onPlantClick = onPlantClick,
                    )
                }
            }
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
        val itemsAvailable = plantItemSize(plantUiState)
        val scrollbarState = state.scrollbarState(itemsAvailable = itemsAvailable)
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

private fun plantItemSize(
    plantUiState: PlantUiState,
) = when (plantUiState) {
    is PlantUiState.Error -> 0 // Nothing
    PlantUiState.Loading -> 1 // Loading bar
    is PlantUiState.Success -> 2 + plantUiState.plants.size // Toolbar, header
}

@Composable
private fun PlantToolbar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = PsIcons.Back,
                contentDescription = stringResource(R.string.back),
            )
        }
    }
}

private fun LazyListScope.plantBody(
    plants: PlantUiState,
    onPlantClick: (String) -> Unit,

) {
    plantResourceCards(plants, onPlantClick)
}

private fun LazyListScope.plantResourceCards(
    plants: PlantUiState,
    onPlantClick: (String) -> Unit,
) {
    when (plants) {
        is PlantUiState.Success -> {
            plantCardItems(
                items = plants.plants,
                onClick = onPlantClick,
                itemModifier = Modifier.padding(8.dp),
            )
        }

        PlantUiState.Loading -> item {
            CircularProgressIndicator()
        }

        is PlantUiState.Error -> item {
            Text("Error")
        }
    }
}

@Preview
@Composable
fun PlantScreen_Preview() {
    PsTheme {
        PsBackground {
            PlantScreen(
                plantUiState = PlantUiState.Loading,
                onBackClick = {},
                onPlantClick = {},
            )
        }
    }
}
