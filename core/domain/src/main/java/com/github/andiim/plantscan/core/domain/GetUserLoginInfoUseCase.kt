package com.github.andiim.plantscan.core.domain

import com.github.andiim.plantscan.core.auth.AuthHelper
import com.github.andiim.plantscan.core.data.repository.UserDataRepository
import com.github.andiim.plantscan.core.model.data.LoginInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetUserLoginInfoUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val authHelper: AuthHelper,
) {
    operator fun invoke(): Flow<LoginInfo> {
        return combine(authHelper.currentUser, userDataRepository.userData) { auth, data ->
            if (auth.id.isEmpty()) {
                authHelper.createAnonymousAccount().collect()
            }

            LoginInfo(
                userId = data.userId.ifBlank { auth.id },
                isAnonymous = auth.isAnonymous,
            )
        }
    }
}
