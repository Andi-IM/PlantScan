package com.github.andiim.plantscan.app.core.data.utils

import androidx.paging.PagingData
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.ClassResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.FamilyResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.GenusResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.OrderResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PhylumResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PlantDetailResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PlantResponse

object DataDummy {
  const val ERROR_FAIL_MESSAGE = "fail"

  private fun String.removeWhitespace() = filterNot { it.isWhitespace() }

  var PLANTS =
      List(10) {
        PlantResponse(
            id = "id@$it",
            name = "name@$it",
            species = "species",
            type = "type",
            images = null,
            commonName = listOf(),
            detail = null)
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
          phylumRef = "phylum")
  fun searchPlant(query: String) =
      PLANTS.filter { plant ->
        plant.name.removeWhitespace() == query.removeWhitespace() ||
            plant.species.removeWhitespace() == query.removeWhitespace()
      }
}
