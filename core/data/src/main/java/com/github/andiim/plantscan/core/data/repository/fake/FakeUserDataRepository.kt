package com.github.andiim.plantscan.core.data.repository.fake

import com.github.andiim.plantscan.core.data.repository.UserDataRepository
import com.github.andiim.plantscan.core.datastore.AppPreferencesDataSource
import com.github.andiim.plantscan.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Fake implementation of the [UserDataRepository] that returns hardcoded user data.
 *
 * This allows us to run the app with fake data, without needing an internet connection or working
 * backend.
 */
class FakeUserDataRepository @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource,
) : UserDataRepository {
    override val userData: Flow<UserData> = appPreferencesDataSource.userData

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        appPreferencesDataSource.setShouldHideOnboarding(shouldHideOnboarding)
    }
}
