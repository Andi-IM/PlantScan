package com.github.andiim.plantscan.feature.detect

import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.github.andiim.plantscan.core.common.result.Resource
import com.github.andiim.plantscan.core.designsystem.component.ErrorState
import com.github.andiim.plantscan.core.designsystem.component.LoadingState
import com.github.andiim.plantscan.core.designsystem.theme.PlantScanTheme
import com.github.andiim.plantscan.model.data.DetectResult
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.DecimalFormat
import kotlinx.coroutines.launch

@Composable
fun DetectScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetectViewModel = hiltViewModel(),
) {
  val context = LocalContext.current

  val image =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, viewModel.imageUri)
        ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
          decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
          decoder.isMutableRequired = true
        }
      } else {
        @Suppress("DEPRECATION")
        MediaStore.Images.Media.getBitmap(context.contentResolver, viewModel.imageUri)
      }

  Crossfade(
      targetState = viewModel.interpreter.collectAsState().value,
      modifier = modifier,
      label = "get_model_animated",
  ) { targetState ->
    when (targetState) {
      is Resource.Loading -> {
        LoadingState("Getting Model")
      }
      is Resource.Error -> {
        ErrorState(targetState.exception?.localizedMessage)
      }
      is Resource.Success -> {
        val labels = BufferedReader(InputStreamReader(context.assets.open("labels.txt")))
        viewModel.detect(labels, image, targetState.data)

        when (val detectionState = viewModel.detectResult.collectAsState().value) {
          is Resource.Loading -> LoadingState("Trying to Detect...")
          is Resource.Success -> {
            val painter = rememberAsyncImagePainter(viewModel.imageUri)
            DetectContent(painter, detectionState.data, onBack)
          }
          else -> {}
        }
      }
    }
  }

  LaunchedEffect(viewModel) { viewModel.loadInterpreter(context) }
}

@Composable
fun DetectContent(painter: Painter, detectResult: DetectResult, onBack: () -> Unit) {
  val progress = remember { Animatable(initialValue = 0f) }
  val scope = rememberCoroutineScope()

  val dec = DecimalFormat("#.##")
  val percentage = dec.format(progress.value * 100)

  LaunchedEffect(key1 = progress) {
    scope.launch {
      progress.animateTo(
          targetValue = detectResult.prob,
          animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing))
    }
  }

  Column(modifier = Modifier.fillMaxSize()) {
    Box(Modifier.wrapContentSize()) {
      Image(
          painter = painter,
          modifier = Modifier.height(300.dp).fillMaxWidth().background(Color.Black),
          contentScale = ContentScale.Fit,
          contentDescription = "${detectResult.label} Image")

      IconButton(
          onClick = onBack,
          modifier = Modifier.padding(top = 8.dp, start = 8.dp),
          colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)) {
            Icon(
                Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(24.dp))
          }
    }

    Column(modifier = Modifier.padding(8.dp)) {
      Text("This is maybe...", style = (MaterialTheme.typography).headlineSmall)
      Spacer(modifier = Modifier.height(20.dp))
      Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(detectResult.label)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              CircularProgressIndicator(
                  progress = progress.value,
                  strokeCap = StrokeCap.Round,
                  trackColor = Color.Gray.copy(alpha = 0.5f))
              Text("${percentage}%", fontWeight = FontWeight.Bold)
            }
          }
    }
  }
}

@Preview
@Composable
private fun Preview_DetectContent() {
  PlantScanTheme {
    Surface {
      DetectContent(painterResource(R.drawable.orchid), DetectResult("Testing", 0.5f), onBack = {})
    }
  }
}
