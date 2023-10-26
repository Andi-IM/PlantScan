package com.github.andiim.plantscan.app.core.data

import com.github.andiim.plantscan.app.core.analytics.AnalyticsHelper
import com.github.andiim.plantscan.app.core.data.datastore.DarkThemeConfig
import com.github.andiim.plantscan.app.core.data.datastore.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserDataRepository {
    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    /**
     * Sets the dark theme config.
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    /**
     * Sets whether the user has completed the onboarding process.
     */
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)
}

class OfflineFirstUserDataRepository @Inject constructor(
    private val analyticsHelper: AnalyticsHelper
) : UserDataRepository {
    override val userData: Flow<UserData>
        get() = TODO("Not yet implemented")

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        TODO("Not yet implemented")
    }

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        TODO("Not yet implemented")
    }

}