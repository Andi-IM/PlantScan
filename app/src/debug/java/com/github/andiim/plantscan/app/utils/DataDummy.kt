package com.github.andiim.plantscan.app.utils

import androidx.paging.PagingData
import com.github.andiim.plantscan.app.core.domain.model.Imgz
import com.github.andiim.plantscan.app.core.domain.model.ObjectDetection
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.Prediction
import com.github.andiim.plantscan.app.core.domain.model.Taxonomy
import com.github.andiim.plantscan.app.core.firestore.model.PlantResponse

object DataDummy {
    var OBJECT_DETECTIONS =
        List(10) {
            ObjectDetection(
                image = Imgz(
                    width = 100.0f,
                    height = 100.0f
                ),
                predictions = listOf(
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

    /*var PREDICTIONS =
        List(10) {
            Prediction(
                confidence = 10.0f,
                x = 10.0f,
                y = 10.0f,
                width = 10.0f,
                height = 10.0f,
                jsonMemberClass = "Something $it"
            )
        }*/


    const val ERROR_FAIL_MESSAGE = "fail"

    private fun String.removeWhitespace() = filterNot { it.isWhitespace() }

    private var KNOWN_NAMES = (0..3).map { "name$it" }
    var PLANTS =
        List(10) {
            Plant(
                id = "id@$it",
                name = "name@$it",
                species = "species",
                images = listOf(),
                commonName = KNOWN_NAMES,
                thumbnail = "",
                description = "",
                taxon = Taxonomy("", "", "", "", ""),
            )
        }


    fun getPlants(list: List<PlantResponse>): PagingData<PlantResponse> = PagingData.from(list)

    fun searchPlant(query: String) =
        PLANTS.filter { plant ->
            plant.name.removeWhitespace() == query.removeWhitespace() ||
                    plant.species.removeWhitespace() == query.removeWhitespace()
        }
}
