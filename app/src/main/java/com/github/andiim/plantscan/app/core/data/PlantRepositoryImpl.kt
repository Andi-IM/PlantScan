package com.github.andiim.plantscan.app.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.core.data.mediator.PlantPagingSource
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.FirestoreSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Singleton
class PlantRepositoryImpl
@Inject
constructor(
    private val remote: FirestoreSource,
) : PlantRepository {

    companion object {
        fun getDefaultPageConfig(): PagingConfig {
            return PagingConfig(
                pageSize = PlantPagingSource.NETWORK_PAGE_SIZE, enablePlaceholders = false
            )
        }
    }

    override fun getAllPlant(): Flow<PagingData<Plant>> =
        Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = { PlantPagingSource(remote = remote) })
            .flow

    override fun getPlantDetail(id: String): Flow<Resource<Plant>> = flow {
        val response = remote.getPlantById(id)

    }

    override fun searchPlant(query: String): PagingSource<Int, Plant> =
        PlantPagingSource(remote = remote)

}
