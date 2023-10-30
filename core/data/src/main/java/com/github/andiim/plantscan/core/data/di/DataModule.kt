package com.github.andiim.plantscan.core.data.di

import com.github.andiim.plantscan.core.data.repository.CameraRepository
import com.github.andiim.plantscan.core.data.repository.DefaultCameraRepository
import com.github.andiim.plantscan.core.data.repository.DefaultDetectHistoryRepo
import com.github.andiim.plantscan.core.data.repository.DefaultPlantRepository
import com.github.andiim.plantscan.core.data.repository.DefaultRecentSearchRepository
import com.github.andiim.plantscan.core.data.repository.DefaultSearchContentsRepository
import com.github.andiim.plantscan.core.data.repository.DefaultUserDataRepository
import com.github.andiim.plantscan.core.data.repository.DetectHistoryRepo
import com.github.andiim.plantscan.core.data.repository.DetectRepository
import com.github.andiim.plantscan.core.data.repository.JustOnlineDetectRepository
import com.github.andiim.plantscan.core.data.repository.PlantRepository
import com.github.andiim.plantscan.core.data.repository.RecentSearchRepository
import com.github.andiim.plantscan.core.data.repository.SearchContentsRepository
import com.github.andiim.plantscan.core.data.repository.UserDataRepository
import com.github.andiim.plantscan.core.data.util.ConnectivityManagerNetworkMonitor
import com.github.andiim.plantscan.core.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsCameraRepository(
        cameraRepository: DefaultCameraRepository,
    ): CameraRepository

    @Binds
    fun bindDetectHistoryRepository(
        detectHistoryRepo: DefaultDetectHistoryRepo,
    ): DetectHistoryRepo

    @Binds
    fun bindDetectRepository(
        detectRepository: JustOnlineDetectRepository,
    ): DetectRepository

    @Binds
    fun bindPlantRepository(
        plantRepository: DefaultPlantRepository,
    ): PlantRepository

    @Binds
    fun bindsRecentSearchRepository(
        recentSearchRepository: DefaultRecentSearchRepository,
    ): RecentSearchRepository

    @Binds
    fun bindsSearchContentsRepository(
        searchContentsRepository: DefaultSearchContentsRepository,
    ): SearchContentsRepository

    @Binds
    fun bindsUserDataRepository(
        userDataRepository: DefaultUserDataRepository,
    ): UserDataRepository

    @Binds
    fun bindsNetworkModule(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor
}
