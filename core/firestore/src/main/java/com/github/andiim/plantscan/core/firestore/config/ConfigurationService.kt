package com.github.andiim.plantscan.core.firestore.config

interface ConfigurationService {
    suspend fun fetchConfiguration(): Boolean
    val isShowTaskEditButtonConfig: Boolean
    val mlModelName: String
}