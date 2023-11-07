package com.github.andiim.plantscan.feature.detect

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.github.andiim.plantscan.core.designsystem.component.PsButton
import com.github.andiim.plantscan.core.designsystem.component.PsTopAppBar
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.core.model.data.ObjectDetection
import com.github.andiim.plantscan.core.bitmap.asImageFromBase64

@Composable
fun DetectRoute(
    onBackClick: () -> Unit,
    onSuggestClick: (String) -> Unit,
    viewModel: DetectViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?, SnackbarDuration?) -> Boolean,
) {
    val context = LocalContext.current
    with(viewModel) {
        when (status) {
            DetectStatus.Preview -> {
                DetectPreview(
                    uri = uri,
                    isDialogShow = showDialog,
                    onBackPressed = {
                        onBackClick()
                        viewModel.deleteImageFromUri(context)
                    },
                    onDetectClick = { detect(context) },
                )
            }

            DetectStatus.Result -> {
                when (val state = uiState) {
                    DetectUiState.Loading -> Unit
                    is DetectUiState.Error -> LaunchedEffect(state.message) {
                        onShowSnackbar(state.message.toString(), null, null)
                        onBackClick()
                    }

                    is DetectUiState.Success -> {
                        DetectScreen(
                            onBackClick = onBackClick,
                            onSuggestClick = { onSuggestClick(userId) },
                            result = state.detection,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetectPreview(
    isDialogShow: Boolean,
    uri: Uri,
    onBackPressed: () -> Unit,
    onDetectClick: () -> Unit,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .size(Size.ORIGINAL).build(),
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Image(
            painter = painter,
            null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
        )
        Column(modifier = Modifier.align(Alignment.TopStart)) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape,
                    ),
            ) {
                Icon(PsIcons.Back, null, tint = Color.Black)
            }
        }

        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            PsButton(onClick = onDetectClick) {
                Text(text = "Detect this image")
            }
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }

        if (isDialogShow) {
            AlertDialogLoading()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogLoading() {
    AlertDialog(onDismissRequest = {}) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
                Text("Detecting...")
            }
        }
    }
}

private const val PERCENTAGE = 100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetectScreen(
    onBackClick: () -> Unit,
    onSuggestClick: () -> Unit,
    result: ObjectDetection,
) {
    val scrollState = rememberLazyListState()
    var showPreview by remember { mutableStateOf(false) }
    val imgBitmap = result.image.base64?.asImageFromBase64()

    Box {
        LazyColumn(state = scrollState, horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }
            item {
                PsTopAppBar(
                    titleRes = R.string.detect_screen_title,
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(PsIcons.Back, null)
                        }
                    },
                )
            }
            item {
                DetectImage(image = imgBitmap, onImageClick = { showPreview = !showPreview })
            }

            if (result.predictions.isEmpty()) {
                item { Text(text = stringResource(R.string.predict_is_empty)) }
            }

            result.predictions.forEach { data ->
                item {
                    ListItem(
                        headlineContent = { Text(text = data.jsonMemberClass) },
                        trailingContent = { Text(text = "${data.confidence.times(PERCENTAGE)}%") },
                    )
                }
            }

            item { SuggestButton(onClick = onSuggestClick) }
        }

        if (showPreview) {
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(imgBitmap).build(),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black)
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxSize()
                )

                Column(modifier = Modifier.align(Alignment.TopStart)) {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                    IconButton(
                        onClick = { showPreview = !showPreview },
                        modifier = Modifier
                            .padding(16.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = CircleShape,
                            ),
                    ) {
                        Icon(PsIcons.Close, null, tint = Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun DetectImage(
    image: Bitmap?,
    onImageClick: () -> Unit,
) {
    val imageSize = 32
    SubcomposeAsyncImage(
        model =
        ImageRequest.Builder(LocalContext.current)
            .data(image)
            .size(imageSize)
            .crossfade(true)
            .build(),
        loading = {
            if (LocalInspectionMode.current) {
                ImageInspection()
            } else {
                Box(modifier = Modifier.height(30.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.width(24.dp))
                }
            }
        },
        error = {
            Box(modifier = Modifier.height(30.dp), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Dangerous,
                    contentDescription = stringResource(R.string.fetch_image_error),
                )
            }
        },
        contentDescription = "image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .animateContentSize()
            .clickable { onImageClick() },
    )
}

@Composable
fun ImageInspection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.orchid),
            contentScale = ContentScale.FillHeight,
            contentDescription = null,
        )
    }
}

@Composable
fun SuggestButton(onClick: () -> Unit) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle()) { append(context.getString(R.string.suggestion_button_label)) }
        append(" ")
        withStyle(
            style =
            SpanStyle(color = (MaterialTheme.colorScheme).primary, fontWeight = FontWeight.Bold),
        ) {
            append(context.getString(R.string.suggestion_action_label))
        }
    }

    ClickableText(
        text = annotatedText,
        onClick = { onClick() },
    )
}
