package com.github.andiim.plantscan.feature.camera.component

import android.annotation.SuppressLint
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.dynamicanimation.animation.SpringForce.DAMPING_RATIO_NO_BOUNCY
import androidx.lifecycle.LifecycleOwner
import com.github.andiim.plantscan.core.designsystem.component.PsTextButton
import com.github.andiim.plantscan.feature.camera.INITIAL
import com.github.andiim.plantscan.feature.camera.SCALE_TARGET
import com.github.andiim.plantscan.feature.camera.SPRING_DAMPING_RATIO
import com.github.andiim.plantscan.feature.camera.SPRING_STIFFNESS
import com.github.andiim.plantscan.feature.camera.SPRING_STIFFNESS_ALPHA_OUT
import com.github.andiim.plantscan.feature.camera.extensions.cameraExtensionsName
import com.github.andiim.plantscan.feature.camera.model.CameraExtensionItem
import com.github.andiim.plantscan.feature.camera.model.CameraUIAction
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@SuppressLint("RestrictedApi")
@Composable
fun CameraPreviewView(
    camera: Camera?,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    extensionMode: Int,
    availableExtensions: List<Int>,
    onPreviewStart: (LifecycleOwner, PreviewView) -> Unit,
    cameraUIAction: (CameraUIAction) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    val scale = remember { Animatable(INITIAL) }
    val alpha = remember { Animatable(INITIAL) }
    val coroutineScope = rememberCoroutineScope()

    suspend fun blinkAnimation() {
        val scaleSpec = spring<Float>(SPRING_DAMPING_RATIO, SPRING_STIFFNESS)
        val alphaSpec = spring<Float>(DAMPING_RATIO_NO_BOUNCY, SPRING_STIFFNESS_ALPHA_OUT)
        coroutineScope {
            launch {
                scale.animateTo(SCALE_TARGET, animationSpec = scaleSpec)
                scale.animateTo(INITIAL, animationSpec = scaleSpec)
            }
            launch {
                alpha.animateTo(1f, animationSpec = alphaSpec)
                alpha.animateTo(INITIAL, animationSpec = alphaSpec)
            }
        }
    }

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }
    LaunchedEffect(lensFacing, camera) {
        onPreviewStart.invoke(lifecycleOwner, previewView)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        coroutineScope.launch {
                            cameraUIAction(CameraUIAction.Scale(zoom))
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures {
                        offset = it
                        val meteringPointFactory = previewView.meteringPointFactory
                        val focusPoint = meteringPointFactory.createPoint(it.x, it.y)

                        coroutineScope.launch {
                            blinkAnimation()
                            cameraUIAction(CameraUIAction.Focus(focusPoint))
                        }
                    }
                },
        )

        FocusPoint(
            scale = scale.value,
            alpha = alpha.value,
            modifier = Modifier
                .offset { offset.toIntOffset() }
                .size(64.dp),
        )

        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Bottom,
        ) {
            ShowExtensions(
                mode = extensionMode,
                data = availableExtensions,
                onExtClick = { cameraUIAction(CameraUIAction.SelectCameraExtension(it)) },
            )
            CameraControls(cameraUIAction)
        }
    }
}

@Composable
fun ShowExtensions(
    mode: Int,
    data: List<Int>,
    onExtClick: (Int) -> Unit,
) {
    val context = LocalContext.current
    val list = data.map {
        CameraExtensionItem(
            it,
            context.getString(cameraExtensionsName[it]!!),
            mode == it,
        )
    }
    LazyRow {
        items(list) {
            PsTextButton(onClick = { onExtClick(it.extensionMode) }) {
                if (it.selected) {
                    Box(modifier = Modifier.background(color = Color.Blue)) {
                        Text(it.name, color = Color.White)
                    }
                } else {
                    Text(text = it.name)
                }
            }
        }
    }
}

private fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())
