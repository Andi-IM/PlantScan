package com.github.andiim.plantscan.core.firestore.firestore

import com.github.andiim.plantscan.core.firestore.model.PlantCollectionData
import com.github.andiim.plantscan.core.firestore.model.PlantQuery
import kotlinx.coroutines.flow.Flow

interface FirestoreCollections {
  fun getPlants(query: PlantQuery? = null): Flow<List<PlantCollectionData>>
  fun getDetailFromId(id: String): Flow<PlantCollectionData>
}
