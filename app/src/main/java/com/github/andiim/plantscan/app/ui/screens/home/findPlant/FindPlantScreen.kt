package com.github.andiim.plantscan.app.ui.screens.home.findPlant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.ui.TrackScreenViewEvent
import com.github.andiim.plantscan.app.ui.common.composables.PlantPagedList
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.github.andiim.plantscan.app.R.string as AppText

@Composable
fun FindPlantRoute(
    modifier: Modifier = Modifier,
    onDetails: (String) -> Unit,
    toDetect: () -> Unit,
    toPlantType: () -> Unit,
    viewModel: FindPlantViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()

    FindPlantScreen(
        modifier = modifier,
        query = query,
        data = viewModel.items,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::onSearch,
        toDetail = onDetails,
        toDetect = toDetect,
        toPlantType = toPlantType
    )
}

@Composable
fun FindPlantScreen(
    modifier: Modifier = Modifier,
    query: String = "",
    data: Flow<PagingData<Plant>> = flowOf(),
    onQueryChange: (String) -> Unit = {},
    onSearch: (String) -> Unit = {},
    toDetect: () -> Unit = {},
    toDetail: (String) -> Unit = {},
    toPlantType: () -> Unit = {},
) {
    val plants = data.collectAsLazyPagingItems()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .testTag("Find Plant Content")
    ) {
        DetectButton(
            onClick = toDetect,
            modifier = Modifier.semantics(false) { contentDescription = "Detect Button" }
        )

        Button(
            modifier =
            Modifier
                .align(Alignment.BottomCenter)
                .semantics(true) {
                    contentDescription = "Manual Find Button"
                },
            onClick = toPlantType,
        ) {
            Text(text = stringResource(AppText.search_find_by_type_button))
        }
        // / HERE XXX
        PsSearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            modifier = modifier
                .align(Alignment.TopCenter)
                .testTag("Search Bar")
        ) {
            PlantPagedList(plants = plants, onPlantClick = toDetail)
        }
    }
    TrackScreenViewEvent(screenName = "Home")
}

@Composable
fun DetectButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(modifier = modifier.size(100.dp), onClick = onClick) {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
            modifier
                .fillMaxSize()
                .background(
                    brush =
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF789885), Color(0xFF7D8A82)),
                        center = Offset(0.5f, 0.5f),
                        radius = 0.2f
                    )
                )
        ) {
            Icon(
                Icons.Default.CameraAlt,
                tint = Color.White,
                modifier = modifier
                    .fillMaxSize()
                    .padding(30.dp)
                    .shadow(8.dp, shape = CircleShape),
                contentDescription = stringResource(AppText.search_using_camera_icon_description)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    var active by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {
            active = false
            onSearch(it)
        },
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text(stringResource(AppText.search_placeholder)) },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = stringResource(AppText.search_icon_description)
            )
        },
        trailingIcon = {
            if (active) {
                IconButton(onClick = { active = false }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(AppText.search_icon_close_description)
                    )
                }
            }
        },
        content = content
    )
}

@Preview
@Composable
private fun Preview_FindPlantContent() {
    PlantScanTheme { Surface { FindPlantScreen() } }
}
