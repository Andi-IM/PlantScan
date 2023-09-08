package com.github.andiim.plantscan.app.ui.screens.suggest

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.core.domain.model.Suggestion
import com.github.andiim.plantscan.app.ui.TrackScreenViewEvent
import com.github.andiim.plantscan.app.ui.common.composables.BasicToolbar
import com.github.andiim.plantscan.app.ui.common.composables.PsCard
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarManager
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme

@Composable
internal fun SuggestRoute(
    popUpScreen: () -> Unit,
    onAuthClick: () -> Unit,
    viewModel: SuggestViewModel = hiltViewModel()
) {
    val uiState: SendingState by viewModel.uiState.collectAsStateWithLifecycle()
    val status: UiState by viewModel.status.collectAsState()
    val data: Suggestion by viewModel.data.collectAsState()

    if (status is UiState.Denied) {
        SnackbarManager.showMessage(
            message = stringResource(R.string.suggest_deny_message),
            label = "Login",
            isError = true,
            action = onAuthClick
        )
        popUpScreen.invoke()
    }

    TrackScreenViewEvent(screenName = "Suggesting: ${viewModel.plantId}")


    SuggestScreen(
        state = uiState,
        onBackClick = popUpScreen,
        description = data.description,
        onSetDescription = viewModel::onDescriptionChange,
        image = data.image,
        onSendClick = viewModel::upload,
        onImageSet = viewModel::onImageSet
    )
}

@Composable
internal fun SuggestScreen(
    state: SendingState,
    description: String,
    image: List<Bitmap>,
    onImageSet: (Context, List<Uri>, Int) -> Unit,
    onBackClick: () -> Unit,
    onSetDescription: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val maxItems = 3
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var openAlertDialog by remember { mutableStateOf(false) }
    if (state is SendingState.Loading) openAlertDialog = true

    val launcher = rememberLauncherForActivityResult(
        contract = PickMultipleVisualMedia(maxItems)
    ) { uris -> if (uris.isNotEmpty()) onImageSet(context, uris, maxItems) }


    Box(modifier = modifier) {

        LazyColumn(
            state = listState, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                BasicToolbar(
                    title = R.string.suggestion_title_screen,
                    leading = {
                        IconButton(onClick = onBackClick) {
                            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
                        }
                    },
                )
            }

            item {

                Text(
                    text = "Help developer to make better plant detection",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                OutlinedTextField(
                    value = description,
                    label = {
                        Text(
                            text = stringResource(R.string.description),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    },
                    onValueChange = onSetDescription,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    minLines = 4,
                    maxLines = 7
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Place some image ${image.size}/$maxItems",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(100.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    image.forEachIndexed { index, data ->
                        item {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current).data(data)
                                    .crossfade(true).build(),
                                modifier = Modifier.size(100.dp),
                                contentScale = ContentScale.Crop,
                                contentDescription = "image@${image[index]}",
                            )
                        }
                    }

                    if (image.size != maxItems) {
                        item {
                            PsCard(
                                onClick = {
                                    launcher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                                }, modifier = Modifier.size(100.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onSendClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Text(stringResource(R.string.suggest_send_label))
                }
            }

        }



        if (openAlertDialog) {
            MinimalDialog(
                state = state,
                onDismissRequest = { openAlertDialog = !openAlertDialog },
            )
        }
    }
}

@Composable
fun MinimalDialog(
    state: SendingState,
    onDismissRequest: () -> Unit,
) {
    var progress by remember { mutableFloatStateOf(0.1f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "Loading Progress"
    )

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            when (state) {
                is SendingState.Error -> {
                    Text(
                        text = "This is a minimal dialog",
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center,
                    )
                }

                SendingState.Initial -> onDismissRequest.invoke()
                is SendingState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        state.progress?.let {
                            if (state.progress.toFloatOrNull() != null) {
                                progress = state.progress.toFloat()
                                Column {
                                    CircularProgressIndicator(progress = animatedProgress)
                                    Text("Uploading image ${animatedProgress}%")
                                }
                            } else {
                                Column {
                                    CircularProgressIndicator()
                                    Text(state.progress)
                                }
                            }
                        }
                    }
                }

                SendingState.Success -> onDismissRequest.invoke()
            }
        }
    }
}

@Preview
@Composable
fun Preview_SuggestScreen() {
    PlantScanTheme {
        Surface {
            SuggestScreen(state = SendingState.Initial,
                description = "",
                image = listOf(),
                onImageSet = { _, _, _ -> },
                onBackClick = { /*TODO*/ },
                onSetDescription = {},
                onSendClick = {})
        }
    }
}