package com.github.andiim.plantscan.app.utils

import androidx.paging.PagingData
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.Taxonomy
import com.github.andiim.plantscan.app.core.firestore.model.ClassResponse
import com.github.andiim.plantscan.app.core.firestore.model.FamilyResponse
import com.github.andiim.plantscan.app.core.firestore.model.GenusResponse
import com.github.andiim.plantscan.app.core.firestore.model.OrderResponse
import com.github.andiim.plantscan.app.core.firestore.model.PhylumResponse
import com.github.andiim.plantscan.app.core.firestore.model.PlantDetailResponse
import com.github.andiim.plantscan.app.core.firestore.model.PlantResponse

object DataDummy {
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

    var PLANT_DETAIL = PlantDetailResponse(classification = "something", description = "something")
    var PHYLUM = PhylumResponse(name = "phylum")
    var CLASSNAME = ClassResponse(name = "class", orders = listOf("order1", "order2"))
    var ORDER = OrderResponse(name = "order")
    var FAMILY = FamilyResponse(name = "family", genuses = listOf("genus1", "genus2"))
    var GENUS =
        GenusResponse(
            id = "1",
            name = "genus",
            classRef = "class",
            familyRef = "family",
            orderRef = "order",
            phylumRef = "phylum"
        )

    fun searchPlant(query: String) =
        PLANTS.filter { plant ->
            plant.name.removeWhitespace() == query.removeWhitespace() ||
                    plant.species.removeWhitespace() == query.removeWhitespace()
        }
}
