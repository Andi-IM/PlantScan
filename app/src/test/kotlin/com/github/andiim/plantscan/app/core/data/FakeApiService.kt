package com.github.andiim.plantscan.app.core.data

import com.github.andiim.plantscan.app.core.data.source.firebase.RemotePlantSource
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.DbResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.ClassResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.FamilyResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.GenusResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.OrderResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PhylumResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PlantDetailResponse
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PlantResponse
import com.github.andiim.plantscan.app.core.data.utils.DataDummy
import com.github.andiim.plantscan.app.core.data.utils.State

class FakeApiService : RemotePlantSource {
  companion object {
    var state: State = State.ERROR
  }
  override suspend fun searchPlant(query: String): DbResponse<List<PlantResponse>> {
    return if (state == State.SUCCESS) {
      val data = DataDummy.searchPlant(query)
      DbResponse.Success(data)
    } else {
      DbResponse.Error(DataDummy.ERROR_FAIL_MESSAGE)
    }
  }

  override suspend fun getAllPlant(): DbResponse<List<PlantResponse>> {
    return if (state == State.SUCCESS) {
      DbResponse.Success(DataDummy.PLANTS)
    } else {
      DbResponse.Error(DataDummy.ERROR_FAIL_MESSAGE)
    }
  }

  override suspend fun getPlantDetail(id: String): DbResponse<PlantDetailResponse> {
    return if (state == State.SUCCESS) {
      DbResponse.Success(DataDummy.PLANT_DETAIL)
    } else {
      DbResponse.Error(DataDummy.ERROR_FAIL_MESSAGE)
    }
  }

  override suspend fun getPhylum(id: String): DbResponse<PhylumResponse> {
    return if (state == State.SUCCESS) {
      DbResponse.Success(DataDummy.PHYLUM)
    } else {
      DbResponse.Error(DataDummy.ERROR_FAIL_MESSAGE)
    }
  }

  override suspend fun getClass(phylumId: String, classId: String): DbResponse<ClassResponse> {
    return if (state == State.SUCCESS) {
      DbResponse.Success(DataDummy.CLASSNAME)
    } else {
      DbResponse.Error(DataDummy.ERROR_FAIL_MESSAGE)
    }
  }

  override suspend fun getOrder(id: String): DbResponse<OrderResponse> {
    return if (state == State.SUCCESS) {
      DbResponse.Success(DataDummy.ORDER)
    } else {
      DbResponse.Error(DataDummy.ERROR_FAIL_MESSAGE)
    }
  }

  override suspend fun getFamily(orderId: String, familyId: String): DbResponse<FamilyResponse> {
    return if (state == State.SUCCESS) {
      DbResponse.Success(DataDummy.FAMILY)
    } else {
      DbResponse.Error(DataDummy.ERROR_FAIL_MESSAGE)
    }
  }

  override suspend fun getGenus(ref: String): DbResponse<GenusResponse> {
    return if (state == State.SUCCESS) {
      DbResponse.Success(DataDummy.GENUS)
    } else {
      DbResponse.Error(DataDummy.ERROR_FAIL_MESSAGE)
    }
  }
}
