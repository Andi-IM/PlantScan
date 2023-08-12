package com.github.andiim.plantscan.app.core.data.source.firebase.implement

import com.github.andiim.plantscan.app.core.data.source.firebase.ConfigurationService
import javax.inject.Inject

class FakeConfigurationServiceImpl @Inject constructor(): ConfigurationService {
    override suspend fun fetchConfiguration(): Boolean = true

    override val isShowTaskEditButtonConfig: Boolean
        get() = true
}