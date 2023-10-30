package com.github.andiim.plantscan.core.data.test

import com.github.andiim.plantscan.core.data.di.DataModule
import com.github.andiim.plantscan.core.data.repository.CameraRepository
import com.github.andiim.plantscan.core.data.repository.DetectHistoryRepo
import com.github.andiim.plantscan.core.data.repository.DetectRepository
import com.github.andiim.plantscan.core.data.repository.PlantRepository
import com.github.andiim.plantscan.core.data.repository.RecentSearchRepository
import com.github.andiim.plantscan.core.data.repository.SearchContentsRepository
import com.github.andiim.plantscan.core.data.repository.UserDataRepository
import com.github.andiim.plantscan.core.data.repository.fake.FakeCameraRepository
import com.github.andiim.plantscan.core.data.repository.fake.FakeDetectHistoryRepo
import com.github.andiim.plantscan.core.data.repository.fake.FakeDetectRepository
import com.github.andiim.plantscan.core.data.repository.fake.FakePlantRepository
import com.github.andiim.plantscan.core.data.repository.fake.FakeRecentSearchRepository
import com.github.andiim.plantscan.core.data.repository.fake.FakeSearchContentsRepository
import com.github.andiim.plantscan.core.data.repository.fake.FakeUserDataRepository
import com.github.andiim.plantscan.core.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class],
)
interface TestDataModule {
    @Binds
    fun bindsCameraRepository(
        cameraRepository: FakeCameraRepository,
    ): CameraRepository

    @Binds
    fun bindsDetectRepository(
        cameraRepository: FakeDetectRepository,
    ): DetectRepository

    @Binds
    fun bindsDetectHistoryRepository(
        detectHistoryRepo: FakeDetectHistoryRepo,
    ): DetectHistoryRepo

    @Binds
    fun bindPlantRepository(
        plantRepository: FakePlantRepository,
    ): PlantRepository

    @Binds
    fun bindRecentSearchRepository(
        recentSearchRepository: FakeRecentSearchRepository,
    ): RecentSearchRepository

    @Binds
    fun bindsSearchContentsRepository(
        searchContentsRepository: FakeSearchContentsRepository,
    ): SearchContentsRepository

    @Binds
    fun bindsUserDataRepository(
        userDataRepository: FakeUserDataRepository,
    ): UserDataRepository

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: AlwaysOnlineNetworkMonitor,
    ): NetworkMonitor
}
