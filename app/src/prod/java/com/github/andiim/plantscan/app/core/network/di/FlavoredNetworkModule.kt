package com.github.andiim.plantscan.app.core.network.di

import com.github.andiim.plantscan.app.core.data.source.network.NetworkDataSource
import com.github.andiim.plantscan.app.core.data.source.network.fake.FakeNetworkDataSource
import com.github.andiim.plantscan.app.core.data.source.network.retrofit.RetrofitNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FlavoredNetworkModule {
    @Binds
    fun RetrofitNetwork.binds(): NetworkDataSource
}