package com.github.andiim.plantscan.app.ui.utils

import androidx.paging.PagingData
import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.github.andiim.plantscan.app.core.domain.model.Image
import com.github.andiim.plantscan.app.core.domain.model.Imgz
import com.github.andiim.plantscan.app.core.domain.model.ObjectDetection
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.Prediction
import com.github.andiim.plantscan.app.core.domain.model.Suggestion
import com.github.andiim.plantscan.app.core.domain.model.Taxonomy
import com.github.andiim.plantscan.app.core.firestore.model.PlantDocument
import kotlinx.datetime.Clock

object DataDummy {
    var OBJECT_DETECTIONS = List(10) {
        ObjectDetection(
            image = Imgz(
                width = 100.0f, height = 100.0f
            ), predictions = listOf(
                Prediction(
                    confidence = 10.0f,
                    x = 10.0f,
                    y = 10.0f,
                    width = 10.0f,
                    height = 10.0f,
                    jsonMemberClass = "Something $it"
                )
            )
        )
    }


    private fun String.removeWhitespace() = filterNot { it.isWhitespace() }

    private var KNOWN_NAMES = (0..3).map { "name$it" }
    val TAXONOMY = Taxonomy("GENUS", "CLASS", "FAMILY", "ORDER", "PHYLUM")
    val IMAGE = Image("url", Clock.System.now())
    var PLANTS = List(10) {
        Plant(
            id = "id@$it",
            name = "name@$it",
            species = "species",
            images = listOf(),
            commonName = KNOWN_NAMES,
            thumbnail = "",
            description = "",
            taxon = TAXONOMY,
        )
    }

    fun getPlants(list: List<PlantDocument>): PagingData<PlantDocument> = PagingData.from(list)

    val HISTORIES = List(10) {
        DetectionHistory(
            id = "id@$it",
            timestamp = Clock.System.now(),
            plantRef = "ref",
            userId = "id",
            accuracy = 0.1f
        )
    }

}
