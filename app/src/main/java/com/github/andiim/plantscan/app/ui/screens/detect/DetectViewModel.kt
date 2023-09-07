package com.github.andiim.plantscan.app.ui.screens.detect

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ImageDecoder.ALLOCATOR_SOFTWARE
import android.graphics.ImageDecoder.createSource
import android.graphics.ImageDecoder.decodeBitmap
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.andiim.plantscan.app.core.auth.AccountService
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.ui.common.extensions.launchCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import org.tensorflow.lite.Interpreter
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetectViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val useCase: PlantUseCase,
    private val logService: LogService,
    private val authService: AccountService,
) : ViewModel() {

    private val detectArgs: DetectArgs = DetectArgs(savedStateHandle)
    private val imageUri: Uri = detectArgs.uri.toUri()

    private val _detectionResult: MutableStateFlow<DetectUiState> =
        MutableStateFlow(DetectUiState.Loading)
    val detectResult = _detectionResult.asStateFlow()

    fun detect(context: Context) {
        val bitmap = getImage(context, imageUri)


        launchCatching(logService) {
            val currentUser = authService.currentUser.first()

            useCase.detect(bitmap).collectLatest {
                when (it) {
                    is Resource.Error -> {
                        val error = it.message
                        _detectionResult.update { DetectUiState.Error(error) }
                    }

                    Resource.Loading -> {
                        _detectionResult.update { DetectUiState.Loading }
                    }

                    is Resource.Success -> {
                        var data = it.data.apply {
                            image = image.copy(data = bitmap)
                        }

                        val history = data.predictions.first().let { det ->
                            DetectionHistory(
                                plantRef = det.jsonMemberClass,
                                userId = if (currentUser.isAnonymous) "Anonymous" else currentUser.id,
                                accuracy = det.confidence
                            )

                        }
                        val not = useCase.recordDetection(history).first()
                        Timber.d("NOOT=$not")

                        val detectedObjects = data.predictions.map { results ->
                            with(results) {
                                val text = "${jsonMemberClass}, ${confidence.times(100).toInt()}%"
                                val rect = Rect(
                                    (x - (width / 2)).toInt(),
                                    (y - (height / 2)).toInt(),
                                    (x + (width / 2)).toInt(),
                                    (y + (height / 2)).toInt()
                                )
                                BoxWithText(rect, text)
                            }
                        }
                        val visualizedResult = drawDetectionResult(bitmap, detectedObjects)

                        if (detectedObjects.isNotEmpty()) {
                            data = data.apply {
                                image = image.copy(data = visualizedResult)
                            }
                        }

                        _detectionResult.update {
                            DetectUiState.Success(detection = data)
                        }
                    }
                }
            }
        }
    }
}

data class BoxWithText(val box: Rect, val text: String)

private const val MAX_FONT_SIZE = 96F

/**
 * Draw bounding boxes around objects together with the object's name.
 */
private fun drawDetectionResult(
    bitmap: Bitmap,
    detectionResults: List<BoxWithText>
): Bitmap {
    val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(outputBitmap)
    val pen = Paint()
    pen.textAlign = Paint.Align.LEFT

    detectionResults.forEach {
        // draw bounding box
        pen.color = Color.RED
        pen.strokeWidth = 8F
        pen.style = Paint.Style.STROKE
        val box = it.box
        canvas.drawRect(box, pen)

        val tagSize = Rect(0, 0, 0, 0)

        // calculate the right font size
        pen.style = Paint.Style.FILL_AND_STROKE
        pen.color = Color.YELLOW
        pen.strokeWidth = 2F

        pen.textSize = MAX_FONT_SIZE
        pen.getTextBounds(it.text, 0, it.text.length, tagSize)
        val fontSize: Float = pen.textSize * box.width() / tagSize.width()

        // adjust the font size so texts are inside the bounding box
        if (fontSize < pen.textSize) pen.textSize = fontSize

        var margin = (box.width() - tagSize.width()) / 2.0F
        if (margin < 0F) margin = 0F
        canvas.drawText(
            it.text, box.left + margin,
            box.top + tagSize.height().times(1F), pen
        )
    }
    return outputBitmap
}

private fun getImage(
    context: Context,
    imageUri: Uri,
    // width: Int = 224,
    // height: Int = 224,
): Bitmap {
    val image =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            decodeBitmap(createSource(context.contentResolver, imageUri)) { decoder, _, _ ->
                decoder.allocator = ALLOCATOR_SOFTWARE
                decoder.isMutableRequired = true
            }
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }

    // return Bitmap.createScaledBitmap(image, width, height, true)
    return image
}

sealed interface InterpreterUiState {
    data class Success(val interpreter: Interpreter) : InterpreterUiState
    data class Error(val message: String? = null) : InterpreterUiState
    data object Loading : InterpreterUiState
}

sealed interface DetectUiState {
    data class Success(val detection: Any?) : DetectUiState
    data class Error(val message: String? = null) : DetectUiState
    data object Loading : DetectUiState
}