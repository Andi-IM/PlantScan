package com.github.andiim.plantscan.core.domain

import com.github.andiim.plantscan.core.auth.AuthHelper
import com.github.andiim.plantscan.core.model.data.LoginInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserLoginInfoUseCase @Inject constructor(
    private val authHelper: AuthHelper,
) {
    operator fun invoke(): Flow<LoginInfo> {
        return authHelper.currentUser.map { auth ->
            if (!authHelper.hasUser) {
                authHelper.createAnonymousAccount().collect()
            }

            LoginInfo(
                userId = auth.id,
                isAnonymous = auth.isAnonymous,
            )
        }
    }
}
