package com.github.andiim.plantscan.app.core.data.source.firebase

import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.DbResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.ClassResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.FamilyResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.GenusResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.OrderResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PhylumResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PlantDetailResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PlantResponse

interface RemotePlantSource {
  suspend fun searchPlant(query: String): DbResponse<List<PlantResponse>>
  suspend fun getAllPlant(): DbResponse<List<PlantResponse>>
  suspend fun getPlantDetail(id: String): DbResponse<PlantDetailResponse>
  suspend fun getPhylum(id: String): DbResponse<PhylumResponse>
  suspend fun getClass(phylumId: String, classId: String): DbResponse<ClassResponse>
  suspend fun getOrder(id: String): DbResponse<OrderResponse>
  suspend fun getFamily(orderId: String, familyId: String): DbResponse<FamilyResponse>
  suspend fun getGenus(ref: String): DbResponse<GenusResponse>
}
