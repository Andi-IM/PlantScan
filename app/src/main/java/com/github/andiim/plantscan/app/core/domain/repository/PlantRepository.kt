package com.github.andiim.plantscan.app.core.domain.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.andiim.plantscan.app.core.data.mediator.PlantPagingSource.Companion.NETWORK_PAGE_SIZE
import com.github.andiim.plantscan.app.core.domain.model.Plant
import kotlinx.coroutines.flow.Flow

interface PlantRepository {
  companion object {
    fun getDefaultPageConfig(): PagingConfig {
      return PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false)
    }
  }
  fun getAllPlant(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<Plant>>
  fun getMyPlant(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<Plant>>
  fun searchPlant(
      query: String = "",
      pagingConfig: PagingConfig = getDefaultPageConfig()
  ): Flow<PagingData<Plant>>
}
