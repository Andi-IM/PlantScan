package com.github.andiim.plantscan.data.di

import com.github.andiim.plantscan.data.repository.AccountRepository
import com.github.andiim.plantscan.data.repository.AccountRepositoryImpl
import com.github.andiim.plantscan.data.repository.ConfigRepository
import com.github.andiim.plantscan.data.repository.ConfigRepositoryImpl
import com.github.andiim.plantscan.data.repository.DefaultMyGardenRepository
import com.github.andiim.plantscan.data.repository.DefaultPlantRepository
import com.github.andiim.plantscan.data.repository.DefaultRecentSearchRepository
import com.github.andiim.plantscan.data.repository.MLModelRepository
import com.github.andiim.plantscan.data.repository.MLModelRepositoryImpl
import com.github.andiim.plantscan.data.repository.MyGardenRepository
import com.github.andiim.plantscan.data.repository.PlantRepository
import com.github.andiim.plantscan.data.repository.RecentSearchRepository
import com.github.andiim.plantscan.data.util.ConnectivityManagerNetworkMonitor
import com.github.andiim.plantscan.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
  @Binds
  fun bindsPlantRepository(
      plantRepository: DefaultPlantRepository,
  ): PlantRepository

  @Binds
  fun bindsRecentSearchRepository(
      recentSearchRepository: DefaultRecentSearchRepository,
  ): RecentSearchRepository

  @Binds
  fun bindsMyGardenRepository(
      myGardenRepository: DefaultMyGardenRepository,
  ): MyGardenRepository

  @Binds
  fun bindsAccountRepository(
      accountRepository: AccountRepositoryImpl,
  ): AccountRepository

  @Binds
  fun bindsConfigRepository(
      configRepository: ConfigRepositoryImpl,
  ): ConfigRepository

  @Binds
  fun bindsMLDownloaderRepository(
      repository: MLModelRepositoryImpl,
  ): MLModelRepository

  @Binds
  fun bindsNetworkMonitor(
      networkMonitor: ConnectivityManagerNetworkMonitor,
  ): NetworkMonitor
}
