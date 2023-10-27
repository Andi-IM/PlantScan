package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.core.analytics.AnalyticsHelper
import com.github.andiim.plantscan.core.datastore.AppPreferencesDataSource
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
}
