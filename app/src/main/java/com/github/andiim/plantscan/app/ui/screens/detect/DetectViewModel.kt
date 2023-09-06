package com.github.andiim.plantscan.app.ui.screens.detect

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.ViewModel
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.DetectResult
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.ConfigurationService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.MLService
import com.github.andiim.plantscan.app.ui.common.extensions.launchCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.tensorflow.lite.Interpreter
import timber.log.Timber
import java.io.BufferedReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject

@HiltViewModel
class DetectViewModel
@Inject
constructor(
    private val mlService: MLService,
    private val config: ConfigurationService,
    private val logService: LogService
) : ViewModel() {

    private val _interpreter: MutableStateFlow<Resource<Interpreter>> =
        MutableStateFlow(Resource.Loading)
    val interpreter = _interpreter.asStateFlow()

    private val _detectionResult: MutableStateFlow<Resource<DetectResult>> =
        MutableStateFlow(Resource.Loading)
    val detectResult = _detectionResult.asStateFlow()

    fun loadInterpreter(context: Context) {
        launchCatching(logService) {
            val modelName = config.mlModelName
            mlService.getModel(context, modelName).collect { it ->
                Timber.d("Type= ${it.javaClass} SAMPLING")
                _interpreter.value = it
            }
        }
    }

    fun detect(
        reader: BufferedReader,
        image: Bitmap,
        interpreter: Interpreter,
        width: Int = 224,
        height: Int = 224
    ) {
        val bitmap = Bitmap.createScaledBitmap(image, width, height, true)

        val input =
            ByteBuffer.allocateDirect((width * height) * 3 * 4).order(ByteOrder.nativeOrder())
        for (y in 0 until 224) {
            for (x in 0 until 224) {
                val px = bitmap.getPixel(x, y)

                // Get channel values from pixel value
                val r = Color.red(px)
                val g = Color.green(px)
                val b = Color.blue(px)

                // Normalize channel values to [-1.0, 1.0]. This requirement depends on the model.
                // For example, some models might require values to be normalized to the range
                // [0.0, 1.0] instead.
                val rf = (r - 127) / 255f
                val gf = (g - 127) / 255f
                val bf = (b - 127) / 255f

                input.putFloat(rf)
                input.putFloat(gf)
                input.putFloat(bf)
            }
        }

        val bufferSize = 52 * java.lang.Float.SIZE / java.lang.Byte.SIZE
        val output = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
        interpreter.run(input, output)

        output.rewind()
        val probabilities = output.asFloatBuffer()
        val detections = mutableListOf<DetectResult>()
        for (i in 0 until probabilities.capacity()) {
            if (reader.readLine() == null) continue
            val label: String = reader.readLine()
            val probability: Float = probabilities.get(i)
            detections.add(DetectResult(label, probability))
        }

        val best = detections.maxWithOrNull(compareBy({ it.prob }, { detections.indexOf(it) }))

        best?.let { result -> _detectionResult.update { Resource.Success(result) } }
    }
}
