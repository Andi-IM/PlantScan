package com.github.andiim.plantscan.feature.suggest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.andiim.plantscan.core.designsystem.component.MinimalDialog
import com.github.andiim.plantscan.core.designsystem.component.PsBackground
import com.github.andiim.plantscan.core.designsystem.component.PsTopAppBar
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.core.designsystem.theme.PsTheme
import com.github.andiim.plantscan.core.model.data.Suggestion
import kotlinx.coroutines.launch

@Composable
fun SuggestRoute(
    onBackPressed: () -> Unit,
    onLoginPressed: () -> Unit,
    onShowSnackbar: suspend (String, String?, SnackbarDuration?) -> Boolean,
    viewModel: SuggestViewModel = hiltViewModel(),
) {
    val uiState by viewModel.sendSuggestState.collectAsStateWithLifecycle()
    val userStatus by viewModel.userData.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(uiState, userStatus) {
        if (userStatus is Status.Denied) {
            scope.launch {
                val result = onShowSnackbar(
                    context.getString(R.string.suggestion_deny_message),
                    context.getString(R.string.suggestion_login_snackbar_action),
                    null,
                )
                if (result) {
                    onLoginPressed()
                } else {
                    onBackPressed.invoke()
                }
            }
        }

        if (uiState is SuggestUiState.Complete) {
            scope.launch {
                onShowSnackbar(context.getString(R.string.suggestion_success_message), null, null)
                onBackPressed.invoke()
            }
        }
    }

    SuggestScreen(
        isDialogShow = viewModel.showDialog,
        suggestFormState = viewModel.suggestState,
        userStatus = userStatus,
        onBackPressed = onBackPressed,
        onSuggestSend = {
            viewModel.sendSuggestion(context) {
                scope.launch {
                    onShowSnackbar(it, null, null)
                }
            }
        },
        onSetDescription = viewModel::onDescriptionChange,
        onImageSet = viewModel::onImageSet,
    )
}

private const val MAX_ITEMS = 3

@Composable
fun SuggestScreen(
    isDialogShow: Boolean,
    userStatus: Status,
    suggestFormState: Suggestion,
    onBackPressed: () -> Unit,
    onSuggestSend: () -> Unit,
    onSetDescription: (String) -> Unit,
    onImageSet: (List<Uri>, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val launcher = getMediaLauncher(callback = onImageSet)
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val enabled = userStatus is Status.Granted

    Box(modifier = modifier) {
        LazyColumn {
            toolbar(onBackPressed)
            descriptionEditText(
                enabled,
                suggestFormState.description,
                onSetDescription,
            )
            placeImage(
                enabled,
                suggestFormState.images.map {
                    val contentResolver = context.contentResolver
                    BitmapFactory.decodeStream(contentResolver.openInputStream(Uri.parse(it)))
                },
                MAX_ITEMS,
            ) {
                launcher.launch(PickVisualMediaRequest(ImageOnly))
            }
            item {
                Spacer(modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp))
                Button(
                    onClick = {
                        keyboardController?.hide()
                        onSuggestSend.invoke()
                    },
                    enabled = enabled,
                ) {
                    Text(text = stringResource(id = R.string.suggestion_send_label))
                }
            }
        }
        if (isDialogShow) {
            MinimalDialog(message = stringResource(R.string.suggestion_uploading_message))
        }
    }
}

@Composable
fun getMediaLauncher(
    callback: (List<Uri>, Int) -> Unit,
): ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards Uri>> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(MAX_ITEMS),
    ) { uris ->
        if (uris.isNotEmpty()) callback(uris, MAX_ITEMS)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun LazyListScope.toolbar(onBackClick: () -> Unit) {
    item {
        PsTopAppBar(
            titleRes = R.string.suggestion_title_screen,
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = PsIcons.Back, contentDescription = null)
                }
            },
        )
    }
}

fun LazyListScope.descriptionEditText(
    enabled: Boolean,
    description: String,
    onSetDescription: (String) -> Unit,
) {
    item {
        Text(
            text = "Help developer to make better plant detection",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )

        OutlinedTextField(
            value = description,
            enabled = enabled,
            label = {
                Text(
                    text = stringResource(R.string.suggestion_description),
                    style = MaterialTheme.typography.labelMedium,
                )
            },
            onValueChange = onSetDescription,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            minLines = 4,
            maxLines = 7,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun LazyListScope.placeImage(
    enabled: Boolean,
    image: List<Bitmap>,
    maxItems: Int,
    onImageSet: () -> Unit,
) {
    item {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Place some image ${image.size}/$maxItems",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(100.dp)
                .padding(horizontal = 16.dp),
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
                    Card(
                        onClick = { if (enabled) onImageSet() },
                        modifier = Modifier.size(100.dp),
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview_SuggestScreen() {
    PsTheme {
        PsBackground {
            SuggestScreen(
                isDialogShow = false,
                userStatus = Status.Loading,
                suggestFormState = Suggestion(),
                onBackPressed = {},
                onSuggestSend = {},
                onSetDescription = {},
                onImageSet = { _, _ -> },
            )
        }
    }
}
