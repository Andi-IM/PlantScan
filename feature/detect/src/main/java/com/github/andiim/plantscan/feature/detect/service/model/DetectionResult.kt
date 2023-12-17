package com.github.andiim.plantscan.feature.detect.service.model

import android.os.Parcelable
import com.github.andiim.plantscan.core.model.data.LabelPredict
import com.github.andiim.plantscan.core.model.data.Prediction
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class DetectionResult(
    val id: String? = null,
    val timestamp: Instant? = null,
    val imgB64: String,
    val userId: String,
    val accuracy: Float,
    val detections: List<HashMap<String, String>>,
    val time: Float,
) : Parcelable

private const val OBJECT_CLASS = "objectClass"
private const val CONFIDENCE = "confidence"
fun buildDetectionData(objectClass: String, confidence: Float): HashMap<String, String> {
    return hashMapOf(
        OBJECT_CLASS to objectClass,
        CONFIDENCE to confidence.toString(),
    )
}

fun buildDetectionDataFromPrediction(prediction: Prediction): HashMap<String, String> =
    buildDetectionData(prediction.jsonMemberClass, prediction.confidence)

fun HashMap<String, String>.mapToLabelPredict(): LabelPredict {
    require(this.size == 2) { "Must contain 2 data" }
    val classObject = this[OBJECT_CLASS]!!
    val confidence = this[CONFIDENCE]!!.toFloat()
    return LabelPredict(classObject, confidence)
}
