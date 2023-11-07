package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.analytics.AnalyticsHelper
import com.github.andiim.plantscan.core.datastore.AppPreferencesDataSource
import com.github.andiim.plantscan.core.model.data.DarkThemeConfig
import com.github.andiim.plantscan.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserDataRepository {
    /**
     * Stream of [UserData].
     */
    val userData: Flow<UserData>

    /**
     * Sets whether the user has completed the onboarding process.
     */
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)

    /**
     * Sets the desired dark theme config.
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    /**
     * Sets the preferred dynamic color config.
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    /**
     * Set login information to local store to prevent unlimited request to backend.
     */
    suspend fun setLoginInfo(userId: String = "", isAnonymous: Boolean = true)
}

class DefaultUserDataRepository @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource,
    private val analyticsHelper: AnalyticsHelper,
) : UserDataRepository {
    override val userData: Flow<UserData> = appPreferencesDataSource.userData

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        appPreferencesDataSource.setShouldHideOnboarding(shouldHideOnboarding)
        analyticsHelper.logOnboardingStateChanged(shouldHideOnboarding)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        appPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
        analyticsHelper.logDarkThemeConfigChanged(darkThemeConfig.name)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        appPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
        analyticsHelper.logDynamicColorPreferenceChanged(useDynamicColor)
    }

    override suspend fun setLoginInfo(userId: String, isAnonymous: Boolean) {
        appPreferencesDataSource.setUserData(userId, isAnonymous)
        analyticsHelper.logLoginInfo(userId, isAnonymous)
    }
}
