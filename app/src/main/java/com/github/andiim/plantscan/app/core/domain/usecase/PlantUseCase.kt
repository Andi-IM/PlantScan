package com.github.andiim.plantscan.app.core.domain.usecase

import android.net.Uri
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.data.mediator.PlantPagingSource
import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.github.andiim.plantscan.app.core.domain.model.ObjectDetection
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.Suggestion
import com.github.andiim.plantscan.app.core.firestore.model.ImageContent
import kotlinx.coroutines.flow.Flow
import java.io.File

interface PlantUseCase {
    companion object {
        fun getDefaultPageConfig(): PagingConfig {
            return PagingConfig(
                pageSize = PlantPagingSource.NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            )
        }
    }

    fun getPlants(query: String = ""): PagingSource<Int, Plant>
    fun getPlantDetail(id: String): Flow<Resource<Plant>>
    fun detect(base64ImageData: String): Flow<Resource<ObjectDetection>>
    fun recordDetection(detection: DetectionHistory): Flow<String>
    fun getDetectionsList(userId: String): Flow<Resource<List<DetectionHistory>>>
    fun sendSuggestion(suggestion: Suggestion): Flow<String>
    fun notifyImageCreated(savedUri: Uri)
    fun createImageOutputFile(): File
}
