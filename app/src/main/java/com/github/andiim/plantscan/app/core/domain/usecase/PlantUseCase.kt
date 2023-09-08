package com.github.andiim.plantscan.app.core.domain.usecase

import android.graphics.Bitmap
import android.net.Uri
import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.github.andiim.plantscan.app.core.domain.model.ObjectDetection
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.Suggestion
import kotlinx.coroutines.flow.Flow
import java.io.File

interface PlantUseCase {
    fun getPlants(query: String = ""): PagingSource<Int, Plant>
    fun getPlantDetail(id: String): Flow<Resource<Plant>>
    fun detect(image: Bitmap): Flow<Resource<ObjectDetection>>
    fun recordDetection(detection: DetectionHistory): Flow<String>
    fun getDetectionsList(userId: String): Flow<Resource<List<DetectionHistory>>>
    fun sendSuggestion(suggestion: Suggestion): Flow<Resource<String>>
    fun notifyImageCreated(savedUri: Uri)
    fun createImageOutputFile(): File
}
