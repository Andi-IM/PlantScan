package com.github.andiim.plantscan.app.data.firebase.implement

import com.github.andiim.plantscan.app.data.firebase.ConfigurationService
import javax.inject.Inject

class ConfigurationServiceImpl @Inject constructor(): ConfigurationService {
    override suspend fun fetchConfiguration(): Boolean = true

    override val isShowTaskEditButtonConfig: Boolean
        get() = true
}