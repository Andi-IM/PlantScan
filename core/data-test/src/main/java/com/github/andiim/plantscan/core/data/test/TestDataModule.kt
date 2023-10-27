package com.github.andiim.plantscan.core.data.test

import com.github.andiim.plantscan.core.data.di.DataModule
import com.github.andiim.plantscan.core.data.repository.RecentSearchRepository
import com.github.andiim.plantscan.core.data.repository.UserDataRepository
import com.github.andiim.plantscan.core.data.repository.fake.FakeRecentSearchRepository
import com.github.andiim.plantscan.core.data.repository.fake.FakeUserDataRepository
import com.github.andiim.plantscan.core.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
interface TestDataModule {
    @Binds
    fun bindsUserDataRepository(
        userDataRepository: FakeUserDataRepository,
    ): UserDataRepository

    @Binds
    fun bindRecentSearchRepository(
        recentSearchRepository: FakeRecentSearchRepository
    ): RecentSearchRepository

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: AlwaysOnlineNetworkMonitor
    ): NetworkMonitor
}
