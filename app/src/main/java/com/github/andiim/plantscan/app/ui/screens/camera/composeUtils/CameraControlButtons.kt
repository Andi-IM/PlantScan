package com.github.andiim.plantscan.app.ui.screens.camera.composeUtils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CameraControlButtons() {

  AnimatedSwitchCameraButton()
}

@Composable
fun AnimatedSwitchCameraButton() {
  var rotated by remember { mutableStateOf(false) }

  val animatedFloat: Float by
      animateFloatAsState(
          targetValue = if (rotated) 360f else 0f,
          animationSpec =
              infiniteRepeatable(
                  animation =
                      tween(
                          2000,
                          easing = LinearEasing,
                      ),
                  repeatMode = RepeatMode.Restart,
              ),
          label = "Rotate")

  // val transition = rememberInfiniteTransition(label = "Rotate")

  /*val animateAngle: Float by
  transition.animateFloat(
      initialValue = 0f,
      targetValue = 360f,
      animationSpec =
          infiniteRepeatable(
              animation = tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
      label = "Rotate box",
  )*/

  Column {
    Icon(
        Icons.Default.ChangeCircle,
        contentDescription = "fan",
        modifier =
            Modifier.clickable { rotated = !rotated }
                .rotate(degrees = animatedFloat)
                .padding(30.dp)
                .size(100.dp))
  }
}

@Preview
@Composable
fun Preview_CameraControlButtons() {
  Surface { CameraControlButtons() }
}
