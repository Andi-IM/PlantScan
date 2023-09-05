package com.github.andiim.plantscan.app.ui.screens.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.core.domain.model.Image
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.Taxonomy
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme

@Composable
fun DetailRoute(
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val detailUiState: DetailUiState by viewModel.detailUiState.collectAsState()

    DetailScreen(
        detailUiState = detailUiState,
        onBackClick = popUpScreen,
        modifier = modifier,
    )

}

@Composable
internal fun DetailScreen(
    modifier: Modifier = Modifier,
    detailUiState: DetailUiState,
    onBackClick: () -> Unit,
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
            when (detailUiState) {
                DetailUiState.Error -> TODO()
                DetailUiState.Loading -> item {
                    CircularProgressIndicator()
                }

                is DetailUiState.Success -> {
                    item {
                        DetailToolbar(
                            onBackClick = onBackClick,
                        )
                    }
                    DetailBody(
                        name = detailUiState.detail.name,
                        thumbnail = detailUiState.detail.thumbnail,
                        species = detailUiState.detail.species,
                        description = detailUiState.detail.description,
                        commonName = detailUiState.detail.commonName,
                        taxonomy = detailUiState.detail.taxon,
                        image = detailUiState.detail.images
                    )
                }
            }
        }
    }
}

@Composable
fun DetailToolbar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)

    ) {
        IconButton(
            onClick = onBackClick,
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .size(32.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
    }

}

@Suppress("FunctionName")
private fun LazyListScope.DetailBody(
    name: String,
    thumbnail: String,
    species: String,
    description: String,
    commonName: List<String>,
    image: List<Image>,
    taxonomy: Taxonomy
) {
    item {
        DetailContent(name, thumbnail, species, description, commonName)
    }
    taxonomyCard(taxonomy)
    if (image.size > 1) imageCard(image)
}

@Composable
private fun DetailContent(
    name: String,
    thumbnail: String,
    species: String,
    description: String,
    commonName: List<String>,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(thumbnail)
            .crossfade(true)
            .build(),
        loading = {
            if (LocalInspectionMode.current) {
                Image(
                    painter = painterResource(R.drawable.orchid),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null,
                )
            } else {
                Box(
                    modifier = modifier.height(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = modifier.width(24.dp))
                }
            }
        },
        error = {
            Box(
                modifier = modifier.height(250.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Dangerous,
                    contentDescription = stringResource(R.string.fetch_image_error)
                )
            }
        },
        contentDescription = "$name image",
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .heightIn(max = 250.dp)
            .fillMaxWidth()
    )

    Text(
        "$name, a species of $species",
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        style = MaterialTheme.typography.headlineSmall
    )
    if (commonName.isNotEmpty()) Text("Also Known As: ${commonName.joinToString()}")
    Divider()
    Text(
        description,
        modifier.padding(top = 24.dp, start = 2.dp, end = 2.dp),
        textAlign = TextAlign.Justify,
        style = MaterialTheme.typography.bodyLarge,
    )
}

private fun LazyListScope.taxonomyCard(taxonomy: Taxonomy) {
    item {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            border = BorderStroke(width = 1.dp, color = Color.Gray)
        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                Text(
                    "Scientific Classification",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                ListItem(headlineContent = {
                    Text("Phylum")
                }, trailingContent = {
                    Text(taxonomy.phylum)
                })
                Divider()
                ListItem(headlineContent = {
                    Text("Class")
                }, trailingContent = {
                    Text(taxonomy.className)
                })
                Divider()
                ListItem(headlineContent = {
                    Text("Order")
                }, trailingContent = {
                    Text(taxonomy.order)
                })
                Divider()
                ListItem(headlineContent = {
                    Text("Family")
                }, trailingContent = {
                    Text(taxonomy.family)
                })
                Divider()
                ListItem(headlineContent = {
                    Text("Genus")
                }, trailingContent = {
                    Text(taxonomy.genus)
                })
            }
        }
    }
}

private fun LazyListScope.imageCard(image: List<Image>) {
    item {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            border = BorderStroke(width = 1.dp, color = Color.Gray)
        ) {

        }
    }
}

@Preview
@Composable
private fun Preview_DetailContent() {
    PlantScanTheme {
        Surface {
            DetailScreen(
                onBackClick = {},
                detailUiState = DetailUiState.Success(
                    detail = Plant(
                        id = "1",
                        name = "Dendrobium",
                        thumbnail = "xx",
                        species = "Dendrobium",
                        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
                        commonName = listOf(),
                        images = listOf(),
                        taxon = Taxonomy(
                            genus = "Dendrobium",
                            className = "Liliopsida",
                            family = "Orchidaceae",
                            order = "Asparagales",
                            phylum = "Tracheophyta"
                        )
                    )
                )
            )
        }
    }
}

@Preview
@Composable
private fun Preview_TopContent() {
    PlantScanTheme {
        DetailToolbar(
            onBackClick = {},
        )
    }
}

@Preview
@Composable
private fun Preview_DetailBody() {
    PlantScanTheme {
        LazyColumn {
            DetailBody(
                name = "Dendrobium",
                thumbnail = "xx",
                species = "Dendrobium",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
                commonName = listOf("hello"),
                taxonomy = Taxonomy(
                    genus = "Dendrobium",
                    className = "Liliopsida",
                    family = "Orchidaceae",
                    order = "Asparagales",
                    phylum = "Tracheophyta"
                ),
                image = listOf()
            )
        }
    }
}
