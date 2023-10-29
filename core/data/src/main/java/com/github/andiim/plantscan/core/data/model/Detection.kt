package com.github.andiim.plantscan.core.data.model

import com.github.andiim.plantscan.core.model.data.Imgz
import com.github.andiim.plantscan.core.model.data.ObjectDetection
import com.github.andiim.plantscan.core.model.data.Prediction
import com.github.andiim.plantscan.core.network.model.DetectionResponse
import com.github.andiim.plantscan.core.network.model.ImgzResponse
import com.github.andiim.plantscan.core.network.model.PredictionResponse

fun DetectionResponse.asExternalModel() = ObjectDetection(
    image = image.asExternalModel(),
    predictions = predictions.map { it.asExternalModel() },
)

fun ImgzResponse.asExternalModel() = Imgz(
    width = width,
    height = height,
    base64 = null,
)

fun PredictionResponse.asExternalModel() = Prediction(
    confidence = confidence,
    x = x,
    y = y,
    width = width,
    height = height,
    jsonMemberClass = jsonMemberClass,
)
