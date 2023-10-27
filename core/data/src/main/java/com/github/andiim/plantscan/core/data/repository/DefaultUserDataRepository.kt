package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.core.analytics.AnalyticsHelper
import com.github.andiim.plantscan.core.data.model.UserDataRepository
import com.github.andiim.plantscan.core.datastore.AppPreferencesDataSource
import com.github.andiim.plantscan.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultUserDataRepository @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource,
    private val analyticsHelper: AnalyticsHelper,
) : UserDataRepository {
    override val userData: Flow<UserData>
        get() = TODO("Not yet implemented")

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        TODO("Not yet implemented")
    }
}