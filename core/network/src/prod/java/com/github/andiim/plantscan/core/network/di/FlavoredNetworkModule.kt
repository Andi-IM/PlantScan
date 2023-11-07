package com.github.andiim.plantscan.core.network.di

import com.github.andiim.plantscan.core.network.PsNetworkDataSource
import com.github.andiim.plantscan.core.network.retrofit.RetrofitPsNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FlavoredNetworkModule {
    @Binds
    fun bindRetrofit(dataSource: RetrofitPsNetwork): PsNetworkDataSource
}
