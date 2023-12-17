package com.github.andiim.plantscan.feature.detect

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.bitmap.asBase64
import com.github.andiim.plantscan.core.bitmap.getBitmap
import com.github.andiim.plantscan.core.data.repository.DetectRepository
import com.github.andiim.plantscan.core.domain.GetUserIdUsecase
import com.github.andiim.plantscan.core.model.data.ObjectDetection
import com.github.andiim.plantscan.core.model.data.Prediction
import com.github.andiim.plantscan.core.result.Result
import com.github.andiim.plantscan.core.result.asResult
import com.github.andiim.plantscan.feature.detect.navigation.DetectArgs
import com.github.andiim.plantscan.feature.detect.service.UploadService
import com.github.andiim.plantscan.feature.detect.service.model.DetectionResult
import com.github.andiim.plantscan.feature.detect.service.model.buildDetectionDataFromPrediction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserLoginInfo: GetUserIdUsecase,
    private val detectRepository: DetectRepository,
) : ViewModel() {
    private val args: DetectArgs = DetectArgs(savedStateHandle)
    val uri = args.detectUri.toUri()

    var status by mutableStateOf<DetectStatus>(DetectStatus.Preview)
        private set
    var showDialog by mutableStateOf(false)
        private set

    var uiState by mutableStateOf<DetectUiState>(DetectUiState.Loading)
        private set

    fun detect(context: Context) {
        showDialog = true
        val img = context.getBitmap(uri)
        viewModelScope.launch {
            detectRepository.detect(img.asBase64()).asResult().collectLatest {
                when (it) {
                    Result.Loading -> {
                        uiState = DetectUiState.Loading
                    }

                    is Result.Success -> {
                        val imgResult = findObjects(it.data.predictions).applyToImage(img)
                        val result = it.data.apply {
                            image = image.copy(base64 = imgResult.asBase64())
                        }
                        val detections =
                            it.data.predictions.map(::buildDetectionDataFromPrediction)

                        result.predictions
                            .find { item ->
                                item == result
                                    .predictions
                                    .maxByOrNull { det -> det.confidence }
                            }?.let { predict ->
                                val intent = Intent(context, UploadService::class.java)
                                val historyData = DetectionResult(
                                    imgB64 = imgResult.asBase64(),
                                    userId = getUserLoginInfo().ifEmpty { "Anonymous" },
                                    accuracy = predict.confidence,
                                    detections = detections,
                                    time = result.time
                                )
                                intent.putExtra(UploadService.EXTRA_DETECTION, historyData)
                                context.startService(intent)
                            }

                        uiState = DetectUiState.Success(result)
                        showDialog = false
                    }

                    is Result.Error -> {
                        uiState = DetectUiState.Error(it.exception?.message)
                        Timber.tag("Camera Error").e(it.exception, "detect: %s", null)
                    }
                }
            }
            status = DetectStatus.Result
        }
    }

    private fun findObjects(predictions: List<Prediction>): List<BoxWithText> =
        predictions.map { results ->
            with(results) {
                val text = "$jsonMemberClass, ${confidence.times(100).toInt()}%"
                val rect = Rect(
                    (x - (width / 2)).toInt(),
                    (y - (height / 2)).toInt(),
                    (x + (width / 2)).toInt(),
                    (y + (height / 2)).toInt(),
                )
                BoxWithText(rect, text)
            }
        }

    private fun List<BoxWithText>.applyToImage(
        image: Bitmap,
        strokeWidth: Float = 8f,
        maxFontSize: Float = 96f,
    ): Bitmap {
        if (isEmpty()) return image
        val outputBitmap = image.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(outputBitmap)
        val pen = Paint().apply {
            textAlign = Paint.Align.LEFT
        }

        this.forEach {
            // draw bounding box
            pen.color = Color.RED
            pen.strokeWidth = strokeWidth
            pen.style = Paint.Style.STROKE
            val box = it.box
            canvas.drawRect(box, pen)

            val tagSize = Rect(0, 0, 0, 0)

            // calculate the right font size
            pen.style = Paint.Style.FILL_AND_STROKE
            pen.color = Color.YELLOW
            pen.strokeWidth = 2F

            pen.textSize = maxFontSize
            pen.getTextBounds(it.text, 0, it.text.length, tagSize)
            val fontSize: Float = pen.textSize * box.width() / tagSize.width()

            // adjust the font size so texts are inside the bounding box
            if (fontSize < pen.textSize) pen.textSize = fontSize

            var margin = (box.width() - tagSize.width()) / 2.0F
            if (margin < 0F) margin = 0F
            canvas.drawText(it.text, box.left + margin, box.top + tagSize.height().times(1F), pen)
        }
        return outputBitmap
    }

    fun deleteImageFromUri(context: Context) {
        val contentResolver = context.contentResolver
        contentResolver.delete(uri, null, null)
    }
}

data class BoxWithText(val box: Rect, val text: String)

sealed interface DetectStatus {
    data object Preview : DetectStatus
    data object Result : DetectStatus
}

sealed interface DetectUiState {
    data object Loading : DetectUiState
    data class Success(val detection: ObjectDetection) : DetectUiState
    data class Error(val message: String? = null) : DetectUiState
}
