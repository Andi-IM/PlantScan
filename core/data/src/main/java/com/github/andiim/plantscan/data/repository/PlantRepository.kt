package com.github.andiim.plantscan.data.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.github.andiim.plantscan.core.common.result.Resource
import com.github.andiim.plantscan.core.firestore.model.PlantQuery
import com.github.andiim.plantscan.data.model.PlantResource
import com.github.andiim.plantscan.model.data.Plant
import kotlinx.coroutines.flow.Flow

/** Encapsulation class for query parameter for [PlantResource] */
data class PlantResourceQuery(
    /** Plant types to filter for. Null means any plant type will match. */
    val filterPlantType: Set<String>? = null,
    /** Plant ids to filter for. Null means any plants id will match. */
    val filterPlantIds: Set<String>? = null,
    val filterPlantNames: Set<String>? = null,
) {
  fun toPlantQuery() =
      PlantQuery(
          filterPlantType = this.filterPlantType?.toMutableList(),
          filterPlantIds = this.filterPlantIds?.toMutableList(),
          filterPlantNames = this.filterPlantNames?.toMutableList())
}

/** Data layer implementation for [PlantResource] */
interface PlantRepository {
  companion object {
    const val NETWORK_PAGE_SIZE = 5
    fun getDefaultPageConfig(pageSize: Int = NETWORK_PAGE_SIZE): PagingConfig {
      return PagingConfig(pageSize = pageSize, enablePlaceholders = false)
    }
  }
  fun getPlants(
      query: PlantResourceQuery =
          PlantResourceQuery(
              filterPlantType = null,
              filterPlantIds = null,
              filterPlantNames = null,
          ),
  ): PagingSource<Int, Plant>
  fun getPlantDetail(id: String): Flow<Resource<Plant>>
}
