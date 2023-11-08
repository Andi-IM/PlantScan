package com.github.andiim.plantscan.feature.plantdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.github.andiim.plantscan.core.designsystem.component.PsBackground
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.core.designsystem.theme.PsTheme
import com.github.andiim.plantscan.core.model.data.Plant
import com.github.andiim.plantscan.core.ui.TrackScreenViewEvent
import com.github.andiim.plantscan.core.designsystem.R.drawable as Drawable
import com.github.andiim.plantscan.core.ui.R.string as UiString

@Composable
fun PlantDetailRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlantDetailViewModel = hiltViewModel(),
) {
    val uiState: PlantUiState by viewModel.plantUiState.collectAsStateWithLifecycle()
    TrackScreenViewEvent(screenName = "Plant: ${viewModel.plantId}")
    PlantDetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Composable
fun PlantDetailScreen(
    uiState: PlantUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()
    Box(modifier = modifier) {
        LazyColumn(
            state = state,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }
            when (uiState) {
                PlantUiState.Loading -> item {
                    CircularProgressIndicator()
                }

                is PlantUiState.Success -> {
                    item {
                        Toolbar(onBackClick = onBackClick)
                    }
                    // Body
                    detailBody(uiState.data)
                }
            }
        }
    }
}

@Composable
fun Toolbar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .size(32.dp),
        ) {
            Icon(imageVector = PsIcons.Back, contentDescription = null)
        }
    }
}

private fun LazyListScope.detailBody(
    plant: Plant,
) {
    item {
        DetailContent(
            name = plant.name,
            thumbnail = plant.thumbnail,
            species = plant.species,
            description = plant.description,
            commonName = plant.commonName,
        )
    }
    // TODO: Taxonomy and images
}

@Composable
private fun DetailContent(
    name: String,
    thumbnail: String,
    species: String,
    description: String,
    commonName: List<String>,
    modifier: Modifier = Modifier,
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(thumbnail)
            .crossfade(true)
            .build(),
        loading = {
            if (LocalInspectionMode.current) {
                Image(
                    painter = painterResource(Drawable.orchid),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null,
                )
            } else {
                Box(
                    modifier = modifier.height(250.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(modifier = modifier.width(24.dp))
                }
            }
        },
        error = {
            Box(
                modifier = modifier.height(250.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Dangerous,
                    contentDescription = stringResource(UiString.fetch_image_error),
                )
            }
        },
        contentDescription = "$name image",
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .heightIn(max = 250.dp)
            .fillMaxWidth(),
    )

    Text(
        "$name, a species of $species",
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        style = MaterialTheme.typography.headlineSmall,
    )
    if (commonName.isNotEmpty()) Text("Also Known As: ${commonName.joinToString()}")
    HorizontalDivider()
    Text(
        description,
        modifier.padding(top = 24.dp, start = 2.dp, end = 2.dp),
        textAlign = TextAlign.Justify,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Preview
@Composable
fun PlantDetail_Preview() {
    PsTheme {
        PsBackground {
            PlantDetailScreen(uiState = PlantUiState.Loading, onBackClick = {})
        }
    }
}
