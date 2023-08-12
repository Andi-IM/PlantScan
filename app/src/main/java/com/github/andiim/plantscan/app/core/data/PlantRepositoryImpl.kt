package com.github.andiim.plantscan.app.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.andiim.plantscan.app.core.data.mediator.PlantPagingSource
import com.github.andiim.plantscan.app.core.data.source.firebase.RemotePlantSource
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class PlantRepositoryImpl @Inject constructor(private val remote: RemotePlantSource) :
    PlantRepository {
  override fun getAllPlant(pagingConfig: PagingConfig): Flow<PagingData<Plant>> {
    return Pager(
            config = pagingConfig, pagingSourceFactory = { PlantPagingSource(remote = remote) })
        .flow
  }

  override fun getMyPlant(pagingConfig: PagingConfig): Flow<PagingData<Plant>> {
    TODO("Not yet implemented")
  }

  override fun searchPlant(query: String, pagingConfig: PagingConfig): Flow<PagingData<Plant>> {
    return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PlantPagingSource(remote = remote, query = query) })
        .flow
  }
}
