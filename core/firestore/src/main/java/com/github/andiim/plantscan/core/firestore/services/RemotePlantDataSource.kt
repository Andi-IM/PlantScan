package com.github.andiim.plantscan.core.firestore.services

import com.github.andiim.plantscan.core.common.result.Resource
import com.github.andiim.plantscan.core.firestore.model.ClassCollectionData
import com.github.andiim.plantscan.core.firestore.model.FamilyCollectionData
import com.github.andiim.plantscan.core.firestore.model.GenusCollectionData
import com.github.andiim.plantscan.core.firestore.model.OrderCollectionData
import com.github.andiim.plantscan.core.firestore.model.PhylumCollectionData
import com.github.andiim.plantscan.core.firestore.model.PlantDetailCollectionData
import com.github.andiim.plantscan.core.firestore.model.PlantCollectionData
import com.github.andiim.plantscan.core.firestore.model.PlantTypeCollectionData

/**
 * Interface representing firestore calls to the PlantScan backend
 */
interface RemotePlantDataSource {
  suspend fun searchPlant(query: String): Resource<List<PlantCollectionData>>

  suspend fun getPlantTypes(): Resource<List<PlantTypeCollectionData>>
  suspend fun getAllPlantByTypes(type: String): Resource<List<PlantCollectionData>>
  suspend fun getPlantById(id: String): Resource<PlantCollectionData>
  suspend fun getPlantDetail(id: String): Resource<PlantDetailCollectionData>
  suspend fun getPhylum(id: String): Resource<PhylumCollectionData>
  suspend fun getClass(phylumId: String, classId: String): Resource<ClassCollectionData>
  suspend fun getOrder(id: String): Resource<OrderCollectionData>
  suspend fun getFamily(orderId: String, familyId: String): Resource<FamilyCollectionData>
  suspend fun getGenus(ref: String): Resource<GenusCollectionData>
}
