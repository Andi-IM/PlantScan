package com.github.andiim.plantscan.app.ui.screens.suggest

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.core.domain.model.Suggestion
import com.github.andiim.plantscan.app.ui.TrackScreenViewEvent
import com.github.andiim.plantscan.app.ui.common.composables.BasicField

@Composable
internal fun SuggestRoute(
    popUpScreen: () -> Unit,
    viewModel: SuggestViewModel = hiltViewModel()
) {
    val uiState: SuggestUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val data: Suggestion by viewModel.data.collectAsState()

    TrackScreenViewEvent(screenName = "Suggesting: ${viewModel.plantId}")
    SuggestScreen(
        state = uiState,
        popUpScreen = popUpScreen,
        description = data.description,
        onSetDescription = viewModel::onDescriptionChange,
        image = data.image,
        onSendClick = viewModel::upload,
        onImageSet = viewModel::onImageSet
    )
}

@Composable
internal fun SuggestScreen(
    state: SuggestUiState,
    description: String,
    image: List<Bitmap>,
    onImageSet: (Context, List<Uri>) -> Unit,
    popUpScreen: () -> Unit,
    onSetDescription: (String) -> Unit,
    onSendClick: () -> Unit
) {
    val context = LocalContext.current
    var openAlertDialog by remember { mutableStateOf(false) }
    if (state is SuggestUiState.Loading) openAlertDialog = true

    val launcher = rememberLauncherForActivityResult(
        contract = PickMultipleVisualMedia(5)
    ) { uris -> if (uris.isNotEmpty()) onImageSet(context, uris) }


    Button(onClick = popUpScreen) {
        Text("Back")
    }

    BasicField(
        text = R.string.description,
        value = description,
        onNewValue = onSetDescription,
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        image.forEachIndexed { index, data ->
            item {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data)
                        .crossfade(true)
                        .build(),
                    modifier = Modifier.size(100.dp),
                    contentDescription = "image@${image[index]}",
                )
            }
        }

        item {
            Button(
                onClick = {
                    launcher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                },
            ) {
                Text("Add Image")
            }
        }
    }

    Button(onClick = onSendClick) {
        Text("Send")
    }



    if (openAlertDialog) {
        MinimalDialog(
            state = state,
            onDismissRequest = { openAlertDialog = !openAlertDialog },
        )
    }
}

@Composable
fun MinimalDialog(
    state: SuggestUiState,
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
                is SuggestUiState.Error -> {
                    Text(
                        text = "This is a minimal dialog",
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center,
                    )
                }

                SuggestUiState.Initial -> onDismissRequest.invoke()
                is SuggestUiState.Loading -> {
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

                SuggestUiState.Success -> onDismissRequest.invoke()
            }
        }
    }
}