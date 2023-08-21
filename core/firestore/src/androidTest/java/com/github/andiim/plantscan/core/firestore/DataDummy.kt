package com.github.andiim.plantscan.core.firestore

import com.github.andiim.plantscan.core.firestore.model.ClassCollectionData
import com.github.andiim.plantscan.core.firestore.model.FamilyCollectionData
import com.github.andiim.plantscan.core.firestore.model.GenusCollectionData
import com.github.andiim.plantscan.core.firestore.model.OrderCollectionData
import com.github.andiim.plantscan.core.firestore.model.PhylumCollectionData
import com.github.andiim.plantscan.core.firestore.model.PlantCollectionData
import com.github.andiim.plantscan.core.firestore.model.PlantDetailCollectionData

object DataDummy {
  const val ERROR_FAIL_MESSAGE = "fail"

  private fun String.removeWhitespace() = filterNot { it.isWhitespace() }

  private var KNOWN_NAMES = (0..3).map { "name$it" }
  var PLANTS =
      List(10) {
        PlantCollectionData(
            id = "id@$it",
            name = "name@$it",
            species = "species",
            type = "type",
            images = null,
            commonName = KNOWN_NAMES,
            detail = null)
      }

  // var PLANT_ENTITIES = PLANTS.map { plant -> plant.toEntity() }

//  fun getPlants(list: List<PlantCollectionData>): PagingData<PlantCollectionData> =
//      PagingData.from(list)
//
//  var PLANT_DETAIL =
//      PlantDetailCollectionData(classification = "something", description = "something")
//  var PHYLUM = PhylumCollectionData(id = "1", name = "phylum")
//  var CLASSNAME = ClassCollectionData(name = "class", orders = listOf("order1", "order2"))
//  var ORDER = OrderCollectionData(name = "order")
//  var FAMILY = FamilyCollectionData(name = "family", genuses = listOf("genus1", "genus2"))
//  var GENUS =
//      GenusCollectionData(
//          id = "1",
//          name = "genus",
//          classRef = "class",
//          familyRef = "family",
//          orderRef = "order",
//          phylumRef = "phylum")
//  fun searchPlant(query: String) =
//      PLANTS.filter { plant ->
//        plant.name.removeWhitespace() == query.removeWhitespace() ||
//            plant.species.removeWhitespace() == query.removeWhitespace()
//      }
}
