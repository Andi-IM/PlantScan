package com.github.andiim.plantscan.data.repository

import com.github.andiim.plantscan.core.firestore.config.ConfigurationService
import javax.inject.Inject

interface ConfigRepository {
    suspend fun fetchConfiguration(): Boolean
    val isShowTaskEditButtonConfig: Boolean
    val mlModelName: String
}

class ConfigRepositoryImpl
@Inject
constructor(
    private val config: ConfigurationService,
) : ConfigRepository {
    override suspend fun fetchConfiguration(): Boolean = config.fetchConfiguration()

    override val isShowTaskEditButtonConfig: Boolean = config.isShowTaskEditButtonConfig
    override val mlModelName: String = config.mlModelName
}
