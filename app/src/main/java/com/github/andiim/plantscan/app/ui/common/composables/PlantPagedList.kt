package com.github.andiim.plantscan.app.ui.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.github.andiim.plantscan.app.data.model.Plant

@Composable
fun PlantPagedList(plants: LazyPagingItems<Plant>, onItemClick: (Plant) -> Unit) {
    LazyColumn(state = rememberLazyListState()) {
        items(
            count = plants.itemCount,
            key = plants.itemKey { it.id },
            contentType = plants.itemContentType { "Orchids" }) { index ->
            val orchid = plants[index]
            orchid?.let {
                PlantItem(plant = it, onItemClick)
                Divider()
            }
            Divider()
        }

        plants.apply {
            when (loadState.prepend) {
                is LoadState.NotLoading -> {
                    if (itemCount == 0) {
                        item(key = "not_loading_and_empty") {
                            EmptyItem(modifier = Modifier.fillParentMaxSize())
                        }
                    }
                }

                is LoadState.Loading -> {
                    item { LoadingItem(modifier = Modifier.fillParentMaxSize()) }
                }

                is LoadState.Error -> {
                    val e = loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() })
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingItem(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Refresh Loading"
        )
        CircularProgressIndicator(color = Color.Black)
    }
}

@Composable
private fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
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
        OutlinedButton(onClick = onClickRetry) {
            Text(text = "Try again")
        }
    }
}

@Composable
private fun EmptyItem(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TODO : Adding an empty animation!
            Text(text = "Empty List")
        }
    }
}