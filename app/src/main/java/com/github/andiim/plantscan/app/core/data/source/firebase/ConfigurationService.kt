package com.github.andiim.plantscan.app.core.data.source.firebase

interface ConfigurationService {
    suspend fun fetchConfiguration(): Boolean
    val isShowTaskEditButtonConfig: Boolean
}