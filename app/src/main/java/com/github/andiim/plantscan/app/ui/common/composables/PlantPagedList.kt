package com.github.andiim.plantscan.app.ui.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.github.andiim.plantscan.app.core.domain.model.Plant

@Composable
fun PlantPagedList(
    plants: LazyPagingItems<Plant>,
    onPlantClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()
    LazyColumn(
        state = state,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.semantics(false) { contentDescription = "Plant Lists" },
    ) {
        item {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        }

        plantResourceItems(items = plants, onPlantClick = onPlantClick)

        plants.apply {
            when (loadState.prepend) {
                is LoadState.NotLoading -> {
                    if (itemCount == 0) {
                        notLoadingItem(modifier = Modifier.fillMaxSize())
                    }
                }

                is LoadState.Loading -> {
                    loadingItem(modifier = Modifier.fillMaxSize())
                }

                is LoadState.Error -> {
                    val e = loadState.refresh as LoadState.Error
                    errorItem(
                        message = e.error.localizedMessage!!,
                        modifier = Modifier.fillMaxSize(),
                        onClickRetry = { retry() }
                    )
                }
            }
        }
    }
}

private fun LazyListScope.plantResourceItems(
    items: LazyPagingItems<Plant>,
    onPlantClick: (String) -> Unit,
) = items(
    count = items.itemCount,
    key = items.itemKey { it.id },
    contentType = items.itemContentType { "Orchids" },
    itemContent = { index ->
        val item = items[index]
        item?.let {
            PlantItem(plant = it, onPlantClick)
        }
        Divider()
    }
)

private fun LazyListScope.notLoadingItem(modifier: Modifier) =
    item("not_loading_and_empty") {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TODO : Adding an empty animation!
                Text(text = "Empty List")
            }
        }
    }

private fun LazyListScope.loadingItem(modifier: Modifier) =
    item("loading") {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(modifier = Modifier.padding(8.dp), text = "Refresh Loading")
            CircularProgressIndicator(color = Color.Black)
        }
    }

private fun LazyListScope.errorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) =
    item("error") {
        Row(
            modifier = modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                maxLines = 1,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Red
            )
            OutlinedButton(onClick = onClickRetry) { Text(text = "Try again") }
        }
    }
