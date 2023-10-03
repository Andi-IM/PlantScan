package com.github.andiim.plantscan.app.core.domain.usecase.firebaseServices

interface ConfigurationService {
    suspend fun fetchConfiguration(): Boolean
    val isShowTaskEditButtonConfig: Boolean
    val mlModelName: String
}
